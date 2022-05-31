package com.qunshang.wenpaitong.equnshang.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.adapter.Behavior
import com.qunshang.wenpaitong.equnshang.adapter.HomepageNavBean
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiPersonal
import kotlinx.android.synthetic.main.fragment_personal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.lxj.xpopup.core.PopupInfo
import com.kongzue.dialog.v3.WaitDialog
import kotlinx.android.synthetic.main.item_order_nav.*
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import com.qunshang.wenpaitong.equnshang.view.MyAttachPopupView
import com.tbruyelle.rxpermissions3.RxPermissions
import okhttp3.ResponseBody
import org.json.JSONObject
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.PermissionUtil
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import cn.bertsir.zbar.QrManager

import cn.bertsir.zbar.view.ScanLineView

import cn.bertsir.zbar.QrConfig

/**
 *"我的 "主页面
 * create by 何姝霖
 */
class PersonalFragment: MyBaseFragment(), View.OnClickListener {

    private val apiPersonalTest: ApiPersonal = ApiManager.getInstance().getApiPersonalTest()

    private lateinit var userId: String

    /*网络请求到的数据*/
    private lateinit var userInfo: UserMsgBean.UserInfoBean

    private lateinit var homepageData: HomepageNavBean

    private var shoppingNav: List<ShoppingNavBean> = ArrayList()

    private var homepageNav: List<Behavior> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        /*val  activity = requireActivity() as BaseActivity
        activity.setSystemBarColor(R.color.my_top_bg)*/
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userId = UserInfoViewModel.getUserId()
        val localUserId = UserHelper.getCurrentLoginUser(context)
        
        if (!StringUtils.isEmpty(localUserId)){
            userId = localUserId
        }
        if (StringUtils.isEmpty(userId)){
            return
        } else {
            if (UserInfoViewModel.getUserInfo() == null){
                loadUserInfo()
                return
            }
        }
        initLayout()
        initView()
    }

    fun init(){
        var defaultUserId = UserInfoViewModel.getUserId()

        val localUserId = UserHelper.getCurrentLoginUser(requireContext())
        if (!StringUtils.isEmpty(localUserId)){
            defaultUserId = localUserId
        }

        if (!StringUtils.isEmpty(defaultUserId)) {
            UserInfoViewModel.setUserId(defaultUserId)
            //UserInfoViewModel.setUserId("73")
            Log.i("zhangjunstest", "当前已将userId设置为" + UserInfoViewModel.getUserId())
            ApiManager.getInstance().getApiMainTest()
                .loadPersonalInfo(UserInfoViewModel.getUserId()).enqueue(object :
                    Callback<UserMsgBean> {
                    override fun onResponse(
                        call: Call<UserMsgBean>,
                        response: Response<UserMsgBean>
                    ) {
                        if (response.body() == null) {
                            return
                        }
                        val userInfo = response.body()!!.data
                        UserInfoViewModel.setUserInfo(userInfo)
                        doInit()
                    }

                    override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                    }
                })
        }

    }

    fun doInit(){
        //DialogUtil.toast(requireContext(),"刷新了啊")
        userId = UserInfoViewModel.getUserId()
        val localUserId = UserHelper.getCurrentLoginUser(context)

        if (!StringUtils.isEmpty(localUserId)){
            userId = localUserId
        }
        if (StringUtils.isEmpty(userId)){
            return
        } else {
            if (UserInfoViewModel.getUserInfo() == null){
                loadUserInfo()
                return
            }
        }
        initLayout()
        initView()
    }

    fun loadUserInfo(){
        ApiManager.getInstance().getApiMainTest()
            .loadPersonalInfo(userId).enqueue(object :
                Callback<UserMsgBean> {
                override fun onResponse(
                    call: Call<UserMsgBean>,
                    response: Response<UserMsgBean>
                ) {
                    if (response.body() == null){
                        return
                    }
                    val userInfo = response.body()!!.data
                    UserInfoViewModel.setUserInfo(userInfo)
                    initLayout()
                    initView()
                }
                override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                }
            })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        val activity = requireActivity() as BaseActivity
        if (!hidden){
            /*activity.setFullScreen()*/
            //DialogUtil.toast(context,"舒心了啊")
            init()
        }
    }

    fun initView(){
        tv_personal_introduce.setOnClickListener(this)
        tv_look_order.setOnClickListener(this)
        tv_look_homepage.setOnClickListener(this)
        iv_personal_amall.setOnClickListener(this)
        iv_personal_setting.setOnClickListener(this)
        allorder.setOnClickListener(this)
        waitforpayorder.setOnClickListener(this)
        waitfordeliverorder.setOnClickListener(this)
        waitforgroup.setOnClickListener(this)
        waitgetorder.setOnClickListener(this)
        alreadydoneorder.setOnClickListener(this)
        layout_shitidian.setOnClickListener(this)
        if (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2){
            service_and_tool.visibility = View.GONE
        } else {
            service_and_tool.visibility = View.GONE
        }
        if (Constants.isNewPinHaoHuo){
            layout_old_order.visibility = View.GONE
            layout_old_info.visibility = View.GONE
        } else {
            layout_old_order.visibility = View.VISIBLE
            layout_old_info.visibility = View.VISIBLE
        }
        iv_scan.setOnClickListener(this)
    }

    /**
     * 点击
     */
    override fun onClick(p0: View?) {
        when (p0!!) {
            tv_personal_introduce -> {
                startActivity(Intent(context,UserInfoActivity::class.java))
            }
            iv_personal_amall -> {
                val intent = Intent(requireContext(),QrCodeShareActivity::class.java)
                startActivity(intent)
            }   // 跳转到"AMALL商城"页
            tv_look_order -> {
                context?.startActivity(Intent(context,OrderActivity::class.java))
                return
            }   // 跳转到"我的订单"页
            tv_look_homepage -> {
                context?.startActivity(Intent(context, PersonalHomepageActivity::class.java))
                //toNewFragment(PersonalHomepageActivity())
            }   // 跳转到"我的主页"页
            iv_personal_setting -> {
                showPopMenu()
            }
            allorder -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                goToOrderActivity(0)
            }
            waitforpayorder -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                goToOrderActivity(1)
            }
            waitforgroup -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                goToOrderActivity(2)
            }
            waitfordeliverorder -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                goToOrderActivity(3)
            }
            waitgetorder -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                goToOrderActivity(4)
            }
            alreadydoneorder -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                goToOrderActivity(5)
            }

            layout_shitidian -> {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context,LoginActivity::class.java))
                    return
                }
                startActivity(Intent(requireActivity(),MyPhysicalStoresActivity::class.java))
            }
            iv_scan -> {
                if (PermissionUtil.checkCameraPermission(requireActivity())){
                    scan()
                } else {
                    val rxPermissions = RxPermissions(this)
                    rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe { granted ->
                            if (granted) { // Always true pre-M
                                scan()
                            } else {
                                DialogUtil.toast(requireContext(),"相机权限被拒绝，请打开相机权限使用扫一扫功能")
                            }
                        }
                    //PermissionUtil.requireStoragePermission(this)
                }
            }
        }
    }

    var REQUEST_CODE_SCAN = 300

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        return
    }

    fun analyseScanResult(resultSuccess : String?){
        var index = resultSuccess!!.indexOf("equnshangscan") + 13
        Log.i(Constants.logtag,"idnex是" + index )
        if (index != 12){
            var vendorId = resultSuccess!!.substring(index)
            Log.i(Constants.logtag,"vendro是" + vendorId)
            WaitDialog.show(requireActivity() as AppCompatActivity,"正在打卡")
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
            intent.putExtra("title","活动")
            intent.putExtra("url",resultSuccess + "?userId=" + UserInfoViewModel.getUserId())
            startActivity(intent)
            return
        }

        val _26vericationindex = resultSuccess.indexOf("equnshang/26thVerification")
        if (_26vericationindex != -1){
            val intent = Intent(requireContext(),WebViewActivity::class.java)
            intent.putExtra("title","活动")
            intent.putExtra("url",resultSuccess + "&vId=" + UserInfoViewModel.getUserId())
            Log.i(Constants.logtag,resultSuccess + "&vId=" + UserInfoViewModel.getUserId())
            startActivity(intent)
            return
        }
    }

    fun scan(){
        val qrConfig = QrConfig.Builder()
            //.setDesText("(识别二维码)") //扫描框下文字
            .setShowDes(false) //是否显示扫描框下面文字
            .setShowLight(true) //显示手电筒按钮
            //.setShowTitle(true) //显示Title
            .setShowAlbum(true) //显示从相册选择按钮
            .setCornerColor(Color.WHITE) //设置扫描框颜色
            .setLineColor(Color.WHITE) //设置扫描线颜色
            .setLineSpeed(QrConfig.LINE_MEDIUM) //设置扫描线速度
            .setScanType(QrConfig.TYPE_QRCODE) //设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
            .setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE) //设置扫描框类型（二维码还是条形码，默认为二维码）
            .setCustombarcodeformat(QrConfig.BARCODE_I25) //此项只有在扫码类型为TYPE_CUSTOM时才有效
            .setPlaySound(true) //是否扫描成功后bi~的声音
            .setNeedCrop(true) //从相册选择二维码之后再次截取二维码
            .setIsOnlyCenter(true) //是否只识别框中内容(默认为全屏识别)
            .setTitleText("") //设置Tilte文字
            .setTitleBackgroudColor(Color.TRANSPARENT) //设置状态栏颜色
            //.setTitleTextColor(Color.WHITE) //设置Title文字颜色
            .setShowZoom(false) //是否手动调整焦距
            .setAutoZoom(false) //是否自动调整焦距
            .setFingerZoom(false) //是否开始双指缩放
            .setScreenOrientation(QrConfig.SCREEN_PORTRAIT) //设置屏幕方向
            .setDoubleEngine(false) //是否开启双引擎识别(仅对识别二维码有效，并且开启后只识别框内功能将失效)
            .setOpenAlbumText("选择要识别的图片") //打开相册的文字
            .setLooperScan(false) //是否连续扫描二维码
            .setLooperWaitTime(5 * 1000) //连续扫描间隔时间
            .setScanLineStyle(ScanLineView.style_radar) //扫描动画样式
            .setAutoLight(false) //自动灯光
            .setShowVibrator(false) //是否震动提醒
            .create()
        QrManager.getInstance().init(qrConfig).startScan(
            requireActivity()
        ) { result ->
            var  resultSuccess : String? = result.getContent()
            analyseScanResult(resultSuccess)
        }
        return
    }

    fun goToOrderActivity(index : Int){
        val intent = Intent(requireContext(),OrderActivity::class.java)
        intent.putExtra("index",index)
        startActivity(intent)
    }

    private fun showPopMenu(){
        val attachPopupView =
            MyAttachPopupView(requireContext())
        val popInfo = PopupInfo()
        popInfo.atView = iv_personal_setting
        popInfo.hasShadowBg = false
        attachPopupView.popupInfo = popInfo

        attachPopupView.show()
    }

    /**
     * 根据获取到的数据渲染布局
     */
    private fun initLayout() {
        loadPersonalInfo()  //头像、身份、姓名、签名
        //loadShoppingData()  //商品收藏、店铺关注、浏览记录、优惠劵
        loadHomepageData()  //关注、点赞、收藏、评论
        loadShoppingData()
    }

    /**
     * 获取该用户信息,并存入UserInfoViewModel之中
     */
    private fun loadPersonalInfo() {
        userInfo = UserInfoViewModel.getUserInfo()!!
        Glide.with(this)
            .load(userInfo.headimage)
            .into(iv_personal_head)
        iv_personal_name.text = userInfo.uname

        var identity = ""

        if (!(UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and !(UserInfoViewModel.getUserInfo()!!.is_partner <= 1 )){//既是会员又是合伙人
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3){
                identity = "会员,主任"
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                identity = "会员,总裁"
            } else {
                identity = "会员,店主"
            }
            //identity = "会员,合伙人"
        }
        if (!(UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and (UserInfoViewModel.getUserInfo()!!.is_partner <= 1 )){//既是会员又是合伙人
            identity = "会员"
        }
        if ((UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and !(UserInfoViewModel.getUserInfo()!!.is_partner <= 1 )){//既是会员又是合伙人
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 3){
                identity = "主任"
            } else if (UserInfoViewModel.getUserInfo()!!.getIs_partner() == 4) {
                identity = "总裁"
            } else {
                identity = "店主"
            }
        }
        if ((UserInfoViewModel.getUserInfo()!!.is_vip <= 1) and (UserInfoViewModel.getUserInfo()!!.is_partner <= 1 )){//既是会员又是合伙人
            identity = "粉丝"
        }
        iv_personal_identity.setText(identity)
    }

    override fun onResume() {
        super.onResume()
        if (UserInfoViewModel.getUserInfo() != null){
            iv_personal_name.text = UserInfoViewModel.getUserInfo()?.uname
            Glide.with(this).load(UserInfoViewModel.getUserInfo()!!.headimage).into(iv_personal_head)
        }
        init()
    }

    /**
     * 获取"我的"页面商店信息导航数据（商品收藏、店铺关注、浏览记录、优惠劵）
     */
    private fun loadShoppingData() {
        apiPersonalTest.loadShoppingNav(userId).enqueue(object: Callback<BaseHttpListBean<ShoppingNavBean>>{
           override fun onResponse(
               call: Call<BaseHttpListBean<ShoppingNavBean>>,
               response: Response<BaseHttpListBean<ShoppingNavBean>>
           ) {
               if (response.body() == null){
                   return
               }
               if (response.body()!!.code != 200){
                   return
               }
               if (response.body()!!.data == null){
                   return
               }
               shoppingNav = response.body()!!.data!!
                //shoppingNavAdapter = ShoppingNavAdapter(context!!, shoppingNav, Color.WHITE)
                //gv_personal_shopping.adapter = shoppingNavAdapter
               if (shoppingNav.size >= 4){
                   tv_number_star.text = shoppingNav.get(0).number.toString()
                   tv_number_concern.text = shoppingNav.get(1).number.toString()
                   tv_number_his.text = shoppingNav.get(2).number.toString()
                   tv_number_discount.text = shoppingNav.get(3).number.toString()

                   layout_star.setOnClickListener {
                       if (!UserHelper.isLogin(requireContext())){
                           startActivity(Intent(context,LoginActivity::class.java))
                           return@setOnClickListener
                       }
                       val intent = Intent(context,MyStarActivity::class.java)
                       intent.putExtra("position",0)
                       context?.startActivity(intent)
                   }

                   layout_concern.setOnClickListener {
                       if (!UserHelper.isLogin(requireContext())){
                           startActivity(Intent(context,LoginActivity::class.java))
                           return@setOnClickListener
                       }
                       val intent = Intent(context,MyStarActivity::class.java)
                       intent.putExtra("position",1)
                       context?.startActivity(intent)
                   }

                   layout_his.setOnClickListener {
                       if (!UserHelper.isLogin(requireContext())){
                           startActivity(Intent(context,LoginActivity::class.java))
                           return@setOnClickListener
                       }
                       context?.startActivity(Intent(context,BrowseHistoryActivity::class.java))
                   }

                   layout_discount.setOnClickListener {
                       if (!UserHelper.isLogin(requireContext())){
                           startActivity(Intent(context,LoginActivity::class.java))
                           return@setOnClickListener
                       }
                       context?.startActivity(Intent(context,DiscountActivity::class.java))
                   }

               }
            }

            override fun onFailure(call: Call<BaseHttpListBean<ShoppingNavBean>>, t: Throwable) {
            }
        })


    }

    /**
     * 获取"我的主页"导航数据（关注、点赞、收藏、评论）
     */
    private fun loadHomepageData() {
        apiPersonalTest.loadHomepageNav(userId,userId).enqueue(object: Callback<BaseHttpBean<HomepageNavBean>>{
            override fun onResponse(call: Call<BaseHttpBean<HomepageNavBean>>, response: Response<BaseHttpBean<HomepageNavBean>>) {
                homepageData = response.body()!!.data!!
                homepageNav = homepageData.behavior
                /*homepageNavAdapter = HomepageNavAdapter(context!!,homepageNav, BLACK)
                gv_personal_homepage.adapter = homepageNavAdapter*/
                //PersonalNavViewModel.setHomepageNav(homepageNav)
                if (homepageNav.size >= 4){
                    //DialogUtil.toast(requireContext(),"获取了啊")
                    tv_number_attention.text = homepageNav.get(0).number.toString()
                    tv_number_up.text = homepageNav.get(1).number.toString()
                    tv_number_videostar.text = homepageNav.get(2).number.toString()
                    tv_number_comment.text = homepageNav.get(3).number.toString()
                    layout_upload.visibility = View.GONE
                }
                if (homepageNav.size >= 5){
                    layout_upload.visibility = View.VISIBLE
                    tv_number_upload.text = homepageNav.get(4).number.toString()
                }
                layout_attention.setOnClickListener {
                    if (!UserHelper.isLogin(requireContext())){
                        startActivity(Intent(context,LoginActivity::class.java))
                        return@setOnClickListener
                    }
                    context?.startActivity(Intent(context,ConcernActivity::class.java))
                }
                layout_up.setOnClickListener {
                    if (!UserHelper.isLogin(requireContext())){
                        startActivity(Intent(context,LoginActivity::class.java))
                        return@setOnClickListener
                    }
                    context?.startActivity(Intent(context,UpActivity::class.java))
                }
                layout_videostar.setOnClickListener {
                    if (!UserHelper.isLogin(requireContext())){
                        startActivity(Intent(context,LoginActivity::class.java))
                        return@setOnClickListener
                    }
                    context?.startActivity(Intent(context,StarActivity::class.java))
                }
                layout_comment.setOnClickListener {
                    if (!UserHelper.isLogin(requireContext())){
                        startActivity(Intent(context,LoginActivity::class.java))
                        return@setOnClickListener
                    }
                    context?.startActivity(Intent(context,CommentActivity::class.java))
                }
                layout_upload.setOnClickListener {
                    if (!UserHelper.isLogin(requireContext())){
                        startActivity(Intent(context,LoginActivity::class.java))
                        return@setOnClickListener
                    }
                    context?.startActivity(Intent(context,PublishActivity::class.java))
                }
            }
            override fun onFailure(call: Call<BaseHttpBean<HomepageNavBean>>, t: Throwable) {
            }
        })
    }



}

