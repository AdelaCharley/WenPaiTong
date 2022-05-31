package com.qunshang.wenpaitong.equnshang.activity

import android.Manifest
import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.fragment.*
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.constants.ConstantsAPI
import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.amap.api.maps.MapsInitializer
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.qunshang.wenpaitong.databinding.ActivityMainBinding
import com.lxj.xpopup.XPopup
import com.tbruyelle.rxpermissions3.RxPermissions
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import com.qunshang.wenpaitong.equnshang.fragment.LiuZhuanFragment
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.NewRegisterDialog
import java.lang.Exception

@SuppressLint("HandlerLeak")
class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }*/
        index = 0
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadPersonalInfo()
        initImages()
        initView()
        registerWeChat()
        checkVersion()
        MapsInitializer.updatePrivacyShow(this,true,true)
        MapsInitializer.updatePrivacyAgree(this,true)
        loadPhoneInfo()
        handleUnresovedEvent()
        showNewerDialog()
        //CommonUtil.dp2px(this,1)
        //GlideUtil.getInstance().clearImageAllCache(this)
        //Log.e("zhangjuna", "onCreate: "+JPushInterface.getRegistrationID(this) )
    }

    /*data class ChangeBean (

        var pageIndex: Int = 0

    )*/

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    fun changeCurrentItem(bean : ChangeBean){
        if (bean.pageIndex == 0){
            showFragment(0)
        }
    }*/

    override fun onResume() {
        super.onResume()
        if (Constants.isMainPageNeedToChangePage){
            if (Constants.pageNeedToBeChange != -1){
                showFragment(Constants.pageNeedToBeChange)
                if (fragments.get(1).isAdded){
                    if (Constants.pageNeedToBeChange == 1){
                        var get = fragments.get(1) as WenBanTongActivity
                        get.showShop()
                    }
                }

            }
            Constants.isMainPageNeedToChangePage = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun showNewerDialog(){
        if (StringUtils.isEmpty(UserInfoViewModel.getUserId())){
            return
        }
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .getNewUserGiftTime(UserInfoViewModel.getUserId().toInt())
            .enqueue(object: Callback<BaseHttpBean<NewcomerGiftInfoBean>> {
                override fun onResponse(call: Call<BaseHttpBean<NewcomerGiftInfoBean>>,
                                        response: Response<BaseHttpBean<NewcomerGiftInfoBean>>) {
                    if (response.body() == null){
                        return
                    }
                    if (response.body()?.code != 200){
                        return
                    }
                    if (response.body()?.data?.status == 10 || response.body()?.data?.status == 20){

                    } else {
                        return
                    }
                    val endTime = response.body()?.data?.endTime
                    var timeRemain = TimeUtil.getTimeRemainByDayString(endTime)
                    if (!"00:00:00".equals(timeRemain)) {
                        val dialog = NewRegisterDialog(this@MainActivity,response.body()?.data)
                        XPopup.Builder(this@MainActivity)
                            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                            .enableDrag(true)
                            .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                            .asCustom(dialog)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<BaseHttpBean<NewcomerGiftInfoBean>>,
                    t: Throwable
                ) {

                }

            })

    }

    fun handleUnresovedEvent(){
        if (!StringUtils.isEmpty(Constants.remainedOrderGroupSn) and Constants.isHaveRemainedOrderGroupSn){
            val intent = Intent(this, ParticipateGroupActivity::class.java)
            intent.putExtra("orderGroupSn", Constants.remainedOrderGroupSn)
            startActivity(intent)
            Constants.remainedOrderGroupSn = ""
            Constants.isHaveRemainedOrderGroupSn = false
        }
    }

    fun loadPhoneInfo(){
        try {
            if (!PermissionUtil.checkSysytemPermission(this)){
                PermissionUtil.requireSystemPermission(this)
                return
            }
            var userid = "0"
            if (!StringUtils.isEmpty(UserInfoViewModel.getUserId())){
                userid = UserInfoViewModel.getUserId()
            }
            ApiManager.getInstance().getApiMainTest().uploadPhoneState(
                app_version = CommonUtil.getVersionName(this),
                os = SystemUtil.getSystemVersion(),
                user_id = userid,
                model = SystemUtil.getSystemModel()
            ).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun checkVersion(){
        ApiManager.getInstance().getApiMainTest().checkVersionToUpdate(
            CommonUtil.getVersionCode(this)
        ).enqueue(object : Callback<VersionBean>{
            override fun onResponse(call: Call<VersionBean>, response: Response<VersionBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    TipDialog.show(this@MainActivity,response.body()!!.msg,TipDialog.TYPE.ERROR)
                }
                if (response.body()!!.data.status == 1){
                    showAskDownLoadDialog(response.body()!!)
                }
            }

            override fun onFailure(call: Call<VersionBean>, t: Throwable) {

            }

        })
    }

    fun showAskDownLoadDialog(bean : VersionBean){
        DialogUtil.showUpdateDialog(this,bean)
        return
        MessageDialog.show(this,
            "更新",
            "发现新版本，是否更新",
            "确认")
            .setOkButton(OnDialogButtonClickListener() { baseDialog, v ->
                if (PermissionUtil.checkStoragePermission(this)){
                    baseDialog.doDismiss()
                    download(bean.data.url)
                } else {
                    val rxPermissions = RxPermissions(this)
                        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe { granted ->
                            if (granted) {
                                baseDialog.doDismiss()
                                download(bean.data.url)
                            } else {
                                DialogUtil.toast(this,"存储权限被拒绝，无法下载应用")
                                PermissionUtil.openPermissonInSetting(this)
                            }
                        }
                    return@OnDialogButtonClickListener false
                }
                false
            })
            .cancelable = false
    }

    fun download(url : String){
    }

    fun registerWeChat(){
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APPID, false)

        // 将应用的appId注册到微信

        // 将应用的appId注册到微信
        api.registerApp(Constants.WECHAT_APPID)

        //建议动态监听微信启动广播进行注册到微信

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                // 将该app注册到微信
                api.registerApp(Constants.WECHAT_APPID)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

    val fragments = arrayOf(
        MainFragment(),
        WenBanTongActivity(),
        CollegeListFragment(),
        LiuZhuanFragment(),
        MyFragment()
    )

    lateinit var linearlayouts : Array<View>

    lateinit var imageviews : Array<ImageView>

    lateinit var texts : Array<TextView>

    lateinit var imageDefault : Array<Int>

    lateinit var imagesSelected : Array<Int>//被选中时的状态

    companion object{
        var index = 0
        lateinit var api: IWXAPI
    }

    //先初始化需要使用的图片
    fun initImages(){
        imageDefault = arrayOf(
            R.mipmap.image_mainpage_unselected,
            //R.mipmap.ic_main_lottery_false_new_year,
            R.mipmap.rengou_unselected,
            R.mipmap.image_college_unselected,
            R.mipmap.liuzhuan_unselected,
            //R.mipmap.ic_main_workpoint_false_new_year,
            R.mipmap.image_my_unselected
        )
        imagesSelected = arrayOf(
            R.mipmap.image_mainpage_selected,
            R.mipmap.rengou_selected,
            //R.mipmap.ic_main_lottery_true_new_year,
            R.mipmap.image_college_selected,
            R.mipmap.liuzhuan_selected,
            //R.mipmap.ic_main_workpoint_true_new_year,
            R.mipmap.image_my_selected)
    }

    fun initView(){
        //首先初始化view的数据。
        linearlayouts = arrayOf(
            binding.layoutMain,
            //binding.layoutLingjiang,
            binding.layoutRengou,
            binding.layoutChuangyebang,
            binding.layoutLiuzhuan,
            //binding.layoutGongfen,
            binding.layoutMine)
        imageviews = arrayOf(
            binding.imgMain,
            //binding.imgLingjiang,
            binding.imgRengou,
            binding.imgChuangyebang,
            binding.imgLiuzhuan,
            //binding.imgGongfen,
            binding.imgMine
        )
        texts = arrayOf(
            binding.textMain,
            //binding.textLingjiang,
            binding.textRengou,
            binding.textChuangyebang,
            //binding.textGongfen,
            binding.textLiuzhuan,
            binding.textMine
        )
        for (item in linearlayouts.indices) {
            linearlayouts[item].setOnClickListener(MainLayoutListener((item)))
        }
        showFragment(0)
        //supportFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
    }

    /**
     * 点击物理后退按键，单次提示，两次退出
     */
    private val MESSAGE_BACK = 1
    private val MAX_EXIT_TIME: Long = 2000 //连续两次按[返回键]的最大间隔时间，超出则推出该程序
    private var isFlag = true
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_BACK -> isFlag = true // 在2s时,恢复isFlag的变量值
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && isFlag) {
            isFlag = false
            Toast.makeText(this@MainActivity, "再点击一次返回键退出应用", Toast.LENGTH_SHORT).show()
            handler.sendEmptyMessageDelayed(MESSAGE_BACK, MAX_EXIT_TIME)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    inner class MainLayoutListener(val index : Int) : View.OnClickListener{

        override fun onClick(v: View?) {
            if (MainActivity.index != index){
                showFragment(index)
            }
        }
    }

    fun showFragment(index : Int = 0){
        if (index == 4 && !UserHelper.isLogin(this)){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            return
        }
        imageviews[MainActivity.index].setImageResource(imageDefault[MainActivity.index])
        texts[MainActivity.index].setTextColor(resources.getColor(R.color.black))
        if (MainActivity.index == 0){
            var layoutParams = imageviews[MainActivity.index].layoutParams
            layoutParams.width = CommonUtil.dp2px(this@MainActivity,27)
            layoutParams.height = CommonUtil.dp2px(this@MainActivity,27)
            imageviews[MainActivity.index].layoutParams = layoutParams
        }


        MainActivity.index = index
        imageviews[MainActivity.index].setImageResource(imagesSelected[MainActivity.index])
        texts[MainActivity.index].setTextColor(resources.getColor(R.color.black))

        if (MainActivity.index == 0){
            var layoutParams = imageviews[MainActivity.index].layoutParams
            layoutParams.width = CommonUtil.dp2px(this@MainActivity,27)
            layoutParams.height = CommonUtil.dp2px(this@MainActivity,27)
            imageviews[MainActivity.index].layoutParams = layoutParams
        }

        hideAllFragments()
        StringUtils.log("噢噢噢噢" + index)
        if (fragments[index].isAdded){
            StringUtils.log("我切换到了" + index)
            supportFragmentManager.beginTransaction().show(fragments[index]).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.container,fragments[index]).commit()
        }
    }

    fun hideAllFragments(){
        try {
            val transaction = supportFragmentManager.beginTransaction()
            for (i in fragments.indices){

                if (fragments[i].isAdded && !fragments[i].isHidden){
                    transaction.hide(fragments[i]).commit()
                }
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }


    /**
     * 获取该用户信息,并存入UserInfoViewModel之中
     */
    private fun loadPersonalInfo() {
        var defaultUserId = UserInfoViewModel.getUserId()

        val localUserId = UserHelper.getCurrentLoginUser(this)
        if (!StringUtils.isEmpty(localUserId)){
            defaultUserId = localUserId
        }

        if (!StringUtils.isEmpty(defaultUserId)){
            //UserInfoViewModel.setUserId("73")
            UserInfoViewModel.setUserId(defaultUserId)
            Log.i("zhangjunstest","当前已将userId设置为" + UserInfoViewModel.getUserId())
            ApiManager.getInstance().getApiMainTest()
                .loadPersonalInfo(UserInfoViewModel.getUserId()).enqueue(object :
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
                    }
                    override fun onFailure(call: Call<UserMsgBean>, t: Throwable) {
                    }
                })
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm: InputMethodManager? =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    assert(v != null)
                    imm.hideSoftInputFromWindow(v?.getWindowToken(), 0)
                }
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }

    fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom: Int = top + v.getHeight()
            val right: Int = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

}