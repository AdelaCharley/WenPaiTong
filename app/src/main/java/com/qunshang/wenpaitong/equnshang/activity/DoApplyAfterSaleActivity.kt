package com.qunshang.wenpaitong.equnshang.activity

import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.bumptech.glide.Glide
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_do_apply_after_sale.*
import kotlinx.android.synthetic.main.activity_do_apply_after_sale.layout_newaddress
import kotlinx.android.synthetic.main.activity_do_apply_after_sale.phone
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.order_info.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.adapter.AfterSaleImageAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.ImageUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.io.IOException

class DoApplyAfterSaleActivity : BaseActivity() {

    var orderId = 0

    var addressId = "-2000"

    var type = 0

    companion object{
        val REQUEST_CODE = 200
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_apply_after_sale)
        this.orderId = intent.getIntExtra("orderId",0)
        this.type = intent.getIntExtra("type",0)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        toolbar_back.setOnClickListener { finish() }
        if (type == 10){
            toolbar_title.setText("申请退款")
        }
        if (type == 20){
            toolbar_title.setText("申请退货退款")
        }
        if (type == 30){
            toolbar_title.setText("申请换货")
            tv_return_reason.setText("换货原因")
            layout_tuikuan.visibility = View.GONE
        }
        ApiManager.getInstance().getApiMallTest().loadOrderDetail(orderId.toString()).enqueue(object :
            Callback<OrderDetailBean> {
            override fun onResponse(call: Call<OrderDetailBean>, response: Response<OrderDetailBean>) {
                if (response.body() != null){
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<OrderDetailBean>, t: Throwable) {

            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(str : String){}

    lateinit var bean: OrderDetailBean

    fun initView(orderBean: OrderDetailBean){

        this.bean = orderBean

        reason.setOnClickListener {
            val data: MutableList<String?> = ArrayList()
            data.add("商品与描述不符")
            data.add("质量问题")
            data.add("其他")
            val picker = OptionPicker(this)
            picker.setTitle("货物分类")
            picker.setBodyWidth(CommonUtil.dp2px(this,200))
            picker.cancelView.setTextSize(TypedValue.COMPLEX_UNIT_PX,CommonUtil.dp2px(this,15).toFloat())
            picker.okView.setTextSize(TypedValue.COMPLEX_UNIT_PX,CommonUtil.dp2px(this,15).toFloat())
            picker.setData(data)
            picker.setDefaultPosition(2)
            picker.setBackgroundColor(resources.getColor(R.color.white))
            picker.setOnOptionPickedListener(object : OnOptionPickedListener{
                override fun onOptionPicked(position: Int, item: Any?) {
                    reason.setText(data.get(position))
                }

            })
            val wheelLayout = picker.getWheelLayout()
            wheelLayout.setTextSize(CommonUtil.dp2px(this,15))
            picker.show()
        }

        Glide.with(this).load(orderBean.data.product.skuList.posterUrl).into(img_goods)
        tv_goods_name.setText(orderBean.data.product.productName)
        tv_goods_size.setText("规格:" + orderBean.data.product.skuList.value)
        tv_goods_price.setText(orderBean.data.product.skuList.price.toString())
        tv_goods_quantity.setText("x " + orderBean.data.product.skuList.number)
        tv_return_price.setText("" + orderBean.data.orderPayAmount.toString())
        initAddressLayout()
        adapter = AfterSaleImageAdapter(this)
        images.adapter = adapter
        submit.setOnClickListener {
            submit()
        }
    }

    var uploadedCount = 0

    fun submit(){
        if (StringUtils.isEmpty(reason.text.toString())){
            DialogUtil.showWarnDialog(this,"请输入退款原因")
            return
        }
        if (StringUtils.isEmpty(explain.text.toString())){
            DialogUtil.showWarnDialog(this,"请输入退款说明")
            return
        }
        uploadImages()
    }

    var uploadedImages = ArrayList<String>()

    fun uploadImages(){

        var isError = false

        WaitDialog.show(this,"正在上传")
        if (adapter.getData().size == 0){
            doSubmit()
            return
        }
        ImageUtil.uploadAfterSaleImages(this,bean.data.orderSn,adapter.getData().get(uploadedCount),object : okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                if (!isError){
                    WaitDialog.dismiss()
                    TipDialog.show(this@DoApplyAfterSaleActivity,"上传失败", TipDialog.TYPE.SUCCESS)
                    isError = true
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.body == null){
                    return
                }
                val res = response.body!!.string()
                val json = JSONObject(res)
                if (json.getInt("code") == 200){
                    uploadedCount ++
                    uploadedImages.add(json.getString("data"))
                    if (uploadedCount == adapter.getData().size){
                        doSubmit()
                    } else {
                        uploadImages()
                    }
                } else {
                    if (!isError){
                        WaitDialog.dismiss()
                        TipDialog.show(this@DoApplyAfterSaleActivity,"上传失败", TipDialog.TYPE.SUCCESS)
                        isError = true
                    }
                }
            }

        })
    }

    fun doSubmit(){
        val images = StringUtils.analyseListToString(uploadedImages)
        Log.i(Constants.logtag,images)
        ApiManager.getInstance().getApiAMallV3().submitAfterSaleApply(
            UserInfoViewModel.getUserId()
            ,bean.data.orderSn,type,reason.text.toString(),
        explain.text.toString(),images,addressId).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                WaitDialog.dismiss()
                if (response.body() == null){
                    return
                }

                val json = JSONObject(response.body()!!.string())
                Log.i(Constants.logtag,json.toString())
                if (json.getInt("code") == 200){
                    EventBus.getDefault().post("refresh")
                    DialogUtil.showSuccessDialog(this@DoApplyAfterSaleActivity,"上传成功")
                    val intent = Intent(this@DoApplyAfterSaleActivity,OrderActivityV2::class.java)
                    startActivity(intent)
                } else {
                    DialogUtil.showWarnDialog(this@DoApplyAfterSaleActivity,json.getString("msg"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                WaitDialog.dismiss()
            }

        })
    }

    lateinit var adapter : AfterSaleImageAdapter

    fun initAddressLayout(){
        if (type == 30){
            layout_tuikuan.visibility = View.GONE
            ApiManager.getInstance().getApiPersonalTest().loadDefaultAddress(UserInfoViewModel.getUserId()).enqueue(object :
                Callback<DefaultAddressBean> {
                override fun onResponse(call: Call<DefaultAddressBean>, response: Response<DefaultAddressBean>) {
                    if (response.body() != null){
                        if (response.body()!!.data == null){
                            return
                        }
                        if (!StringUtils.isEmpty(response.body()!!.data?.phone)){

                            layout_newaddress.visibility = View.GONE
                            layout_address.visibility = View.VISIBLE

                            val bean = response.body()?.data!!
                            name.setText(bean.name)
                            phone.setText(bean.phone)
                            addresssite.setText(bean.site)
                            addressId = bean.id
                        }
                    }
                }

                override fun onFailure(call: Call<DefaultAddressBean>, t: Throwable) {

                }

            })
        } else {
            layout_newaddress.visibility = View.GONE
            layout_address.visibility = View.GONE
        }

        layout_newaddress.setOnClickListener { chooseAddress() }
        layout_address.setOnClickListener { chooseAddress() }
    }

    private val TYPE_CHOOSE_ADDRESS = 569

    fun chooseAddress(){
        val intent = Intent(this, AddressActivityV2::class.java)
        startActivityForResult(intent,TYPE_CHOOSE_ADDRESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TYPE_CHOOSE_ADDRESS){
            if (resultCode == AddressAdapter.RESULT_CODE){
                layout_newaddress.visibility = View.GONE
                layout_address.visibility = View.VISIBLE
                val addressdata = data?.getSerializableExtra("address") as AddressBean.DataBean
                this.addressId = addressdata.id
                name.setText(addressdata.name)
                phone.setText(addressdata.phone)
                addresssite.setText(addressdata.site)
                if (addressdata.getIsDefault().equals("1")){
                    label2.visibility = View.VISIBLE
                } else {
                    label2.visibility = View.GONE
                }
            }
        }

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            var mSelected: List<Uri>? = null
            mSelected = Matisse.obtainResult(data)
            adapter.add(mSelected)
        }

    }

}