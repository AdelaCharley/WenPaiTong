package com.qunshang.wenpaitong.equnshang.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bertsir.zbar.QrConfig
import cn.bertsir.zbar.QrManager
import cn.bertsir.zbar.view.ScanLineView
import coil.load
import com.kongzue.dialog.v3.WaitDialog
import com.tbruyelle.rxpermissions3.RxPermissions
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.*
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.adapter.PersonalBottomIconAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.NewcomerGiftInfoBean
import com.qunshang.wenpaitong.equnshang.data.UserFocusCollectNum
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import java.lang.Exception
import java.util.*

class CopyrightPersonalFragment: Fragment() {
    private val TAG = "shu-CopyrightPersonal"
    private lateinit var binding: FragmentCopyrightPersonalBinding
    private lateinit var layoutTop: LayoutCopyrightPersonalTopBinding
    private lateinit var layoutMid: LayoutCopyrightPersonalMidBinding
    private lateinit var bottomIconList: RecyclerView
    private var userId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentCopyrightPersonalBinding.inflate(inflater, container, false)
        layoutTop = binding.layoutTop
        layoutMid = binding.layoutMid
        bottomIconList = binding.layoutBottom.recycleList
        userId = UserHelper.getCurrentLoginUser(requireContext()).toInt()
        return binding.root
    }

    fun goToUser(){
        val intent = Intent(requireContext(),UserInfoActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutTop.imgUserHead.setOnClickListener {
            goToUser()
        }
        binding.layoutTop.tvUserName.setOnClickListener {
            goToUser()
        }
        initLayoutTop()
        initLayoutMid()
        initLayoutBottom()
    }

    /**
     * ????????????????????????????????????????????????
     */
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
    public fun initLayoutTop() {
        var userInfo = UserInfoViewModel.getUserInfo()
        if (userInfo == null) {
            Log.d(TAG, "initLayoutTop: userInfo == null")
            return
        }
        loadCenterInfo()
        layoutTop.imgUserHead.load(userInfo.headimage)
        layoutTop.tvUserName.text = userInfo.uname

        if (userInfo.is_vip == 0) {
            layoutTop.tvVip.background = resources.getDrawable(R.drawable.bg_999999_r2)
            layoutTop.tvVip.text = "??????"
        } else {
            layoutTop.tvVip.background = resources.getDrawable(R.drawable.bg_f05a83_r2)
            layoutTop.tvVip.text = "??????"
        }
        if (userInfo.is_partner == 0) {
            layoutTop.tvRole.visibility = View.GONE
        } else {
            layoutTop.tvRole.visibility = View.VISIBLE
            layoutTop.tvRole.text = when (userInfo.is_partner) {
                2    -> "??????"
                3    -> "?????????"
                4    -> "??????"
                else -> ""
            }
        }

        layoutTop.imgScan.setOnClickListener { }
        layoutTop.imgQrcode.setOnClickListener { }
        layoutTop.layoutCollect.setOnClickListener {
            val intent = Intent(activity, MyCollectionActivity::class.java)
            startActivity(intent)
        }
        layoutTop.layoutConcern.setOnClickListener {
            val intent = Intent(requireContext(), ConcernActivity::class.java)
            startActivity(intent)
        }
        layoutTop.imgQrcode.setOnClickListener {
            val intent = Intent(requireContext(),QrCodeShareActivity::class.java)
            startActivity(intent)
        }
        layoutTop.imgScan.setOnClickListener {
            if (PermissionUtil.checkCameraPermission(requireActivity())){
                scan()
            } else {
                val rxPermissions = RxPermissions(this)
                rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe { granted ->
                        if (granted) { // Always true pre-M
                            scan()
                        } else {
                            DialogUtil.toast(requireContext(),"??????????????????????????????????????????????????????????????????")
                        }
                    }
                //PermissionUtil.requireStoragePermission(this)
            }
        }
    }

    fun scan(){
        val qrConfig = QrConfig.Builder()
            //.setDesText("(???????????????)") //??????????????????
            .setShowDes(false) //?????????????????????????????????
            .setShowLight(true) //?????????????????????
            //.setShowTitle(true) //??????Title
            .setShowAlbum(true) //???????????????????????????
            .setCornerColor(Color.WHITE) //?????????????????????
            .setLineColor(Color.WHITE) //?????????????????????
            .setLineSpeed(QrConfig.LINE_MEDIUM) //?????????????????????
            .setScanType(QrConfig.TYPE_QRCODE) //???????????????????????????????????????????????????????????????????????????????????????
            .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE) //????????????????????????????????????????????????????????????????????????
            .setCustombarcodeformat(QrConfig.BARCODE_I25) //??????????????????????????????TYPE_CUSTOM????????????
            .setPlaySound(true) //?????????????????????bi~?????????
            .setNeedCrop(true) //???????????????????????????????????????????????????
            .setIsOnlyCenter(true) //???????????????????????????(?????????????????????)
            .setTitleText("") //??????Tilte??????
            .setTitleBackgroudColor(Color.TRANSPARENT) //?????????????????????
            //.setTitleTextColor(Color.WHITE) //??????Title????????????
            .setShowZoom(false) //????????????????????????
            .setAutoZoom(false) //????????????????????????
            .setFingerZoom(false) //????????????????????????
            .setScreenOrientation(QrConfig.SCREEN_PORTRAIT) //??????????????????
            .setDoubleEngine(false) //???????????????????????????(???????????????????????????????????????????????????????????????????????????)
            .setOpenAlbumText("????????????????????????") //?????????????????????
            .setLooperScan(false) //???????????????????????????
            .setLooperWaitTime(5 * 1000) //????????????????????????
            .setScanLineStyle(ScanLineView.style_radar) //??????????????????
            .setAutoLight(false) //????????????
            .setShowVibrator(false) //??????????????????
            .create()
        QrManager.getInstance().init(qrConfig).startScan(
            requireActivity()
        ) { result ->
            var  resultSuccess : String? = result.getContent()
            analyseScanResult(resultSuccess)
        }
        return
    }

    fun analyseScanResult(resultSuccess : String?){
        var index = resultSuccess!!.indexOf("equnshangscan") + 13
        Log.i(Constants.logtag,"idnex???" + index )
        if (index != 12){
            var vendorId = resultSuccess!!.substring(index)
            Log.i(Constants.logtag,"vendro???" + vendorId)
            WaitDialog.show(requireActivity() as AppCompatActivity,"????????????")
            ApiManager.getInstance().getApiPersonalTest().signIn(vendorId.toInt(),UserInfoViewModel.getUserId().toInt()).enqueue(
                object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        WaitDialog.dismiss()
                        if (response.body() == null){
                            return
                        }
                        val str = response.body()!!.string()
                        val json = JSONObject(str)
                        if (json.getInt("code") == 200){
                            val intent = Intent(requireContext(),SignInSuccessActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }

                })
            return
        }

        val _26index = resultSuccess.indexOf("equnshang/November26th")
        if (_26index != -1){
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("title","??????")
            intent.putExtra("url",resultSuccess + "?userId=" + UserInfoViewModel.getUserId())
            startActivity(intent)
            return
        }

        val _26vericationindex = resultSuccess.indexOf("equnshang/26thVerification")
        if (_26vericationindex != -1){
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("title","??????")
            intent.putExtra("url",resultSuccess + "&vId=" + UserInfoViewModel.getUserId())
            Log.i(Constants.logtag,resultSuccess + "&vId=" + UserInfoViewModel.getUserId())
            startActivity(intent)
            return
        }
    }

    private fun loadCenterInfo() {
        ApiManager.getInstance()
            .getApiWenBanTong_HeShuLin()
            .loadCenterInfo(userId)
            .enqueue(object: Callback<BaseHttpBean<UserFocusCollectNum>> {
                override fun onResponse(
                    call: Call<BaseHttpBean<UserFocusCollectNum>>,
                    response: Response<BaseHttpBean<UserFocusCollectNum>>
                ) {
                    Log.d(TAG, "onResponse-loadCenterInfo: $response")
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        val focusCollect = data?.focusAndCollect
                        // ???
                        layoutTop.tvConcernNum.text = focusCollect?.focusNum.toString()
                        layoutTop.tvCollectNum.text = focusCollect?.collectNum.toString()
                        // ???
                        val activity1 = data?.activity?.get(0)
                        layoutMid.img1.load(activity1?.image)
                        layoutMid.tvTitle1.text = activity1?.title
                        layoutMid.tvSubtitle1.text = activity1?.subTitle
                        val activity2 = data?.activity?.get(1)
                        layoutMid.img2.load(activity2?.image)
                        layoutMid.tvTitle2.text = activity2?.title
                        layoutMid.tvSubtitle2.text = activity2?.subTitle
                        val activity3 = data?.activity?.get(2)
                        layoutMid.img3.load(activity3?.image)
                        layoutMid.tvTitle3.text = activity3?.title
                        layoutMid.tvSubtitle3.text = activity3?.subTitle
                        //???
                        bottomIconList.adapter =
                            PersonalBottomIconAdapter(requireContext(), data?.tools!!)

                    }
                }

                override fun onFailure(
                    call: Call<BaseHttpBean<UserFocusCollectNum>>,
                    t: Throwable
                ) {
                }

            })
    }

    /**
     * ???????????????????????????
     */
    private fun initLayoutMid() {
        layoutMid.activity1.setOnClickListener {
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("url",Constants.baseURL + "/equnshang/signIn?userId=" + UserInfoViewModel.getUserId())
            intent.putExtra("title","??????")
            startActivity(intent)
        }
        layoutMid.activity2.setOnClickListener {
            val intent = Intent(requireContext(),NewLaiLingJiangActivity::class.java)
            startActivity(intent)
        }
        layoutMid.activity3.setOnClickListener {
            val intent = Intent(requireContext(),ToBeVipOrPartnerActivity::class.java)
            startActivity(intent)
        }
        getCountDown()
    }

    /**
     * ??????????????????
     */
    private fun getCountDown() {
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .getNewUserGiftTime(userId)
            .enqueue(object: Callback<BaseHttpBean<NewcomerGiftInfoBean>> {
                override fun onResponse(call: Call<BaseHttpBean<NewcomerGiftInfoBean>>,
                                        response: Response<BaseHttpBean<NewcomerGiftInfoBean>>) {
                    Log.d(TAG, "onResponse-getNewUserGiftTime: $response")
                    if (response.body() == null){
                        return
                    }
                    if (response.body()?.code != 200){
                        return
                    }
                    if (response.body()?.data?.status == 10 || response.body()?.data?.status == 20){

                    } else {
                        layoutMid.newUserGift.visibility = View.GONE
                        return
                    }
                    val endTime = response.body()?.data?.endTime
                    var timeRemain = TimeUtil.getTimeRemainByDayString(endTime)
                    if (timeRemain == "00:00:00") {
                        layoutMid.newUserGift.visibility = View.GONE
                    } else {
                        val timerTask = object : TimerTask() {
                            override fun run() {
                                timeRemain = TimeUtil.getTimeRemainByDayString(endTime)
                                try {
                                    requireActivity().runOnUiThread {
                                        if (timeRemain == "00:00:00") {
                                            layoutMid.newUserGift.visibility = View.GONE
                                            cancel()
                                        } else {
                                            layoutMid.newUserGift.visibility = View.VISIBLE
                                            layoutMid.tvCountDown.text = "????????????$timeRemain"
                                        }
                                    }
                                } catch (e : Exception){
                                    e.printStackTrace()
                                }

                            }
                        }
                        Timer().schedule(timerTask, 0, 1000)
                        layoutMid.newUserGift.setOnClickListener {
                            val intent = Intent(context, WebViewActivity::class.java)
                            intent.putExtra("title","?????????")
                            intent.putExtra("url", Constants.baseURL + "/equnshang/newcomer?userId=" + UserInfoViewModel.getUserId())
                            context?.startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<NewcomerGiftInfoBean>>,
                                       t: Throwable) {
                }

            })
    }

    /**
     * ???????????????8????????????????????????????????????
     */
    private fun initLayoutBottom() {
        val layoutManager = GridLayoutManager(requireContext(), 4)
        bottomIconList.layoutManager = layoutManager
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        initLayoutTop()
        initLayoutMid()
    }

}