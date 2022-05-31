package com.qunshang.wenpaitong.equnshang.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.annotation.RequiresApi

import com.amap.api.services.core.PoiItem
import com.bumptech.glide.Glide
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.activity_open_store.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.Callback
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import java.io.File
import java.io.IOException

class OpenStoreActivity : BaseActivity (){

    val PICK_PHOTO = 100

    val CHOOSE_ADDRESS = 200

    var imagePath = ""

    var vendorId = 0

    var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_store)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("店铺管理")
        layout_image.setOnClickListener {
            if (PermissionUtil.checkCameraPermission(this)){

            } else {
                PermissionUtil.requireCameraPermission(this)
                return@setOnClickListener
            }
            if (PermissionUtil.checkStoragePermission(this)){

            } else {
                PermissionUtil.requireStoragePermission(this)
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_PHOTO)
        }
        initView()
        showCurrentInfo()
    }

    fun closeInput(){
        layout_image.isClickable = false
        store_name.isFocusableInTouchMode = false
        store_phone.isFocusableInTouchMode = false
        layout_open.isClickable = false
        layout_close.isClickable = false
        layout_storelocation.isClickable = false
        layout_store_info.isClickable = false
    }

    fun initView() {
        layout_open.setOnClickListener {
            TimePickerDialog(
                this,
                AlertDialog.THEME_HOLO_LIGHT,
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        open = StringUtils.addZeroForNum(
                            hourOfDay.toString(),
                            2
                        ) + ":" + StringUtils.addZeroForNum(minute.toString(), 2)
                        opentime.setText(open)
                    }
                },
                0,
                0,
                true
            ).show()
        }
        layout_close.setOnClickListener {
            TimePickerDialog(
                this,
                AlertDialog.THEME_HOLO_LIGHT,
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        close = StringUtils.addZeroForNum(
                            hourOfDay.toString(),
                            2
                        ) + ":" + StringUtils.addZeroForNum(minute.toString(), 2)
                        closetime.setText(close)
                    }
                },
                0,
                0,
                true
            ).show()
        }
        layout_storelocation.setOnClickListener {
            if (!PermissionUtil.checkLocationPermission(this)) {
                PermissionUtil.requireLocationPermission(this)
                return@setOnClickListener
            }
            val intent = Intent(this, AMapChooseActivity::class.java)
            startActivityForResult(intent, CHOOSE_ADDRESS)
        }

        submit.setOnClickListener {
            preSubmit()
        }
        layout_store_info.setOnClickListener {
            val intent = Intent(this, SupplementStoreInfoActivity::class.java)
            startActivityForResult(intent, INFO_REQUEST_CODE)
        }
    }

    companion object {

        val INFO_REQUEST_CODE = 600

        val INFO_RESULT_CODE = 60

        val IMAGE_TYPE_HENDIMAGE = "headImg"

        val IMAGE_TYPE_ALBUM = "album"

    }

    fun showSuccessDialog(str: String?) {
        TipDialog.show(this, str, TipDialog.TYPE.SUCCESS).setOnDismissListener {
            finish()
        }
    }

    fun showErrorDialog(str: String?) {
        TipDialog.show(this, str, TipDialog.TYPE.ERROR)
    }

    fun showWarnDialog(str: String?) {
        TipDialog.show(this, str, TipDialog.TYPE.WARNING)
    }

    fun preSubmit() {
        if (status == 10) {
            TipDialog.show(this, "店铺正在审核中，请耐心等待", TipDialog.TYPE.WARNING)
            return
        }
        if (StringUtils.isEmpty(store_name.text.toString())) {
            showWarnDialog("请输入店铺名")
            return
        }
        if (StringUtils.isEmpty(store_phone.text.toString())) {
            showWarnDialog("请输入店铺手机号")
            return
        }
        if (StringUtils.isEmpty(open)) {
            showWarnDialog("请输入开店时间")
            return
        }
        if (StringUtils.isEmpty(close)) {
            showWarnDialog("请输入关店时间")
            return
        }
        if (StringUtils.isEmpty(lon) and (selectedLatLon == null)) {
            showWarnDialog("请选择实体店位置信息")
            return
        }
        if (StringUtils.isEmpty(imagePath)) {
            showWarnDialog("请上传头像")
        }

        if (imagePath.startsWith("http")) {
            submitInfo(imagePath)
        } else {
            WaitDialog.show(this,"正在上传")
            ImageUtil.uploadStoreImage(IMAGE_TYPE_HENDIMAGE, File(imagePath), object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {

                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.body == null) {
                        return
                    }
                    val str: String = response.body!!.string()
                    if (StringUtils.isEmpty(str)) {
                        return
                    }
                    val json = JSONObject(str)
                    if (json.getInt("code") == 200) {
                        submitInfo(json.getString("data"))
                    }
                }

            })
        }
    }

    fun showCurrentInfo(){
        if (VendorModel.getVendorId() == 0){//暂未开店
            right_back_storelocation.visibility = View.VISIBLE
            right_back_storeinfo.visibility = View.VISIBLE
            return
        }
        ApiManager.getInstance().getApiStore().loadCVendorDetailData(VendorModel.getVendorId()).enqueue(object :
            retrofit2.Callback<CVendorDetailBean> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<CVendorDetailBean>,
                response: Response<CVendorDetailBean>
            ) {
                if (response.body() == null){
                    return
                }
                Log.i(Constants.logtag,"vendorId" + VendorModel.getVendorId())
                if (response.body()!!.code != 200){
                    DialogUtil.toast(this@OpenStoreActivity,response.body()!!.msg)
                    return
                }

                this@OpenStoreActivity.status = response.body()!!.data.status
                Log.i(Constants.logtag,"status" + status)
                if (status == 10){
                    text_status.setText("店铺信息正在审核，请耐心等待！")
                    submit.visibility = View.GONE
                    layout_status.setBackgroundColor(getColor(R.color.aaaorange))
                    closeInput()
                }
                if (status == 20){
                    text_status.setText("审核通过！")
                    submit.setText("确认")
                    layout_status.setBackgroundColor(getColor(R.color.lightgreen))
                    right_back_storelocation.visibility = View.VISIBLE
                    right_back_storeinfo.visibility = View.VISIBLE
                }
                if (status == 30) {
                    text_status.setText(response.body()!!.data.errorMsg)
                    submit.setText("确认")
                    layout_status.setBackgroundColor(getColor(R.color.shenred))
                }

                val bean = response.body()!!.data
                this@OpenStoreActivity.imagePath = bean.vendorHeadImg
                Glide.with(this@OpenStoreActivity).load(imagePath).into(image)

                store_name.setText(bean.vendorName)
                store_phone.setText(bean.vendorPhone)

                vendorlocation = bean.vendorLocation

                this@OpenStoreActivity.vendorLocationName = bean.vendorLocationName
                storelocation.setText(vendorlocation)

                lon = bean.longitude.toString()
                lat = bean.latitude.toString()

                opentime.setText(bean.vendorOpeningTime)
                closetime.setText(bean.vendorCloseTime)

                this@OpenStoreActivity.open = bean.vendorOpeningTime
                this@OpenStoreActivity.close = bean.vendorCloseTime

                storeinfo.setText(bean.vendorDesc)
            }

            override fun onFailure(call: Call<CVendorDetailBean>, t: Throwable) {

            }

        })
    }

    var vendorlocation :String? = ""

    var vendorLocationName  :String? = ""

    var lon : String?= ""

    var lat :String? = ""

    var poiId :String? = ""

    var province :String? = ""

    var city :String? = ""

    var adname :String? = ""

    fun submitInfo(imageUrl : String){
        WaitDialog.show(this,"正在上传")
        if (selectedLatLon != null){
            vendorlocation = selectedLatLon!!.getCityName() + selectedLatLon!!.getAdName() + selectedLatLon!!.getSnippet()
            vendorLocationName = selectedLatLon!!.title
            lon = selectedLatLon!!.latLonPoint.longitude.toString()
            lat = selectedLatLon!!.latLonPoint.latitude.toString()
            poiId = selectedLatLon!!.poiId.toString()
            province = selectedLatLon!!.provinceName
            city = selectedLatLon!!.cityName
            adname = selectedLatLon!!.adName
        }
        ApiManager.getInstance().getApiStore().uploadStoreInfo(VendorModel.getVendorId(),UserInfoViewModel.getUserId(),imageUrl,store_name.text.toString(),
        store_phone.text.toString(),vendorlocation!!,
            vendorLocationName!!,lon!!,lat!!,open,close,
        storeinfo.text.toString(),poiId.toString(),province!!,city!!,adname!!).enqueue(object : retrofit2.Callback<BaseHttpBean<String>>{
            override fun onResponse(
                call: Call<BaseHttpBean<String>>,
                response: Response<BaseHttpBean<String>>
            ) {
                WaitDialog.dismiss()
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code == 200){
                    showSuccessDialog("上传成功")
                } else {
                    showWarnDialog(response.body()!!.msg)
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                WaitDialog.dismiss()
                Log.i(Constants.logtag,t.message!!)
            }

        })
    }

    var open = ""

    var close = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO){
            if (data == null){
                return
            }
            handleImageOnKitKat(data)
        }
        if (requestCode == CHOOSE_ADDRESS){
            if (resultCode == AMapChooseActivity.RESULT_CHOOSE_ADDRESS_SUCCESS){
                if (data == null){
                    return
                }
                handleAddress(data)
            }
        }
        if (requestCode == INFO_REQUEST_CODE){
            if (resultCode == INFO_RESULT_CODE){
                if (data == null){
                    return
                }
                if (!StringUtils.isEmpty(data.getStringExtra("info"))){
                    storeinfo.setText(data.getStringExtra("info"))
                }
            }
        }
    }

    fun handleAddress(data : Intent?){
        val selectedItem : PoiItem? = data?.getParcelableExtra<PoiItem>("data")
        if (selectedItem == null){
            return
        }
        storelocation.setText(selectedItem.getCityName() + selectedItem.getAdName() + selectedItem.getSnippet())
        this.selectedLatLon = selectedItem
    }

    var selectedLatLon : PoiItem? = null

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun handleImageOnKitKat(data: Intent) {
        var imagePath = ""
        val uri: Uri? = data.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.getAuthority()) {
                val id = docId.split(":").toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri?.getAuthority()) {
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public downloads"),
                    java.lang.Long.valueOf(docId)
                )
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri?.getScheme(), ignoreCase = true)) {
            imagePath = getImagePath(uri!!, null)
        } else if ("file".equals(uri?.getScheme(), ignoreCase = true)) {
            imagePath = uri?.getPath()!!
        }
        this.imagePath = imagePath
        Glide.with(this).load(imagePath).into(image)
    }

    @SuppressLint("Range")
    private fun getImagePath(uri: Uri, selection: String?): String {
        var path: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

}