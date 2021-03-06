package com.qunshang.wenpaitong.equnshang.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.WaitDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.utils.*
import java.io.File

class LiuZhuanFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liu_zhuan, container, false)
    }

    var url : String? = ""

    var title : String ?= ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as BaseActivity
        //activity.setSystemBarColor(R.color.black);
        //activity.setSystemBarColor(R.color.black);
        activity.changeToGreyButTranslucent()
        toolbar_back.visibility = View.INVISIBLE
        init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (isHidden) {
        } else {
            val activity = requireActivity() as BaseActivity
            activity.changeToGreyButTranslucent()
        }
    }

    fun init(){
        toolbar_back.setOnClickListener {
            //back()
        }
        url = Constants.baseURL + "/wbt/list"
        StringUtils.log(url)
        title = "??????"
        toolbar_title.setText(title)
        webview.getSettings().setJavaScriptEnabled(true)
        // ????????????????????????
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        //?????????????????????
        webview.getSettings().setUseWideViewPort(true)
        //???????????????
        webview.getSettings().setTextZoom(100)
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webview.addJavascriptInterface(MyHandler(), "buy")
        webview.addJavascriptInterface(NewsHandler(),"share")
        webview.addJavascriptInterface(NewPeopleHandler(),"toReal")
        webview.addJavascriptInterface(LoginHandler(),"login")
        webview.addJavascriptInterface(NewWenBanTongHandler(),"toWbt")
        webview.addJavascriptInterface(VideoHandler(),"toVideo")
        webview.addJavascriptInterface(ShangXueYuanHandler(),"toNews")
        webview.addJavascriptInterface(ToVipHandler(),"toVip")
        webview.addJavascriptInterface(ToWenBanTongHandler(),"toWbtProductList")
        webview.addJavascriptInterface(ToInviteHandler(),"toInvite")
        webview.addJavascriptInterface(FocusHandler(),"focus")
        webview.addJavascriptInterface(AngencyInfoHandler(),"toAgencyInfo")
        webview.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //DialogUtil.toast(this@WebViewActivity,"????????????" + url)
                return false
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

        }


        /*webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = false
        webview.settings.useWideViewPort = true*/
        webview.loadUrl(url!!)
        doComputeNewsPaperTime()
    }

    override fun onResume() {
        super.onResume()
        //init()
        StringUtils.log("??????????????????")
    }

    fun doComputeNewsPaperTime(){
        if (url != null && (url!!.contains("/equnshang/business/news"))){
            TimeUtil.startPaper()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //EventBus.getDefault().unregister(this)
        TimeUtil.pausePaper()
    }

    inner class AngencyInfoHandler {

        @JavascriptInterface
        fun postMessage(str: String?) {
            if (!UserHelper.isLogin(requireContext())){
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                return
            }
            StringUtils.log("??????????????????" + str)
            val json = JSONObject(str)
            val agencyId = json.getInt("agencyId")
            val intent = Intent(requireContext(), V300PublisherMainHomeActivity::class.java)
            intent.putExtra("agencyId",agencyId)
            startActivity(intent)
        }

    }

    inner class FocusHandler {
        @JavascriptInterface
        fun postMessage(str: String?) {
            val json = JSONObject(str)
            val agencyId = json.getInt("agencyId")
            val status = json.getInt("status")
            val followChangeBean = FollowChangeBean()
            followChangeBean.id = agencyId
            if (status == 1) {
                followChangeBean.isFollow = false
            } else {
                followChangeBean.isFollow = true
            }
            EventBus.getDefault().post(followChangeBean)
        }
    }

    inner class ToInviteHandler {
        @JavascriptInterface
        fun postMessage(str: String?) {
            val intent = Intent(requireContext(), QrCodeShareActivity::class.java)
            startActivity(intent)
        }
    }

    inner class ToWenBanTongHandler {

        @JavascriptInterface
        fun postMessage(str: String?){
            val intent = Intent(requireContext(), WenBanTongActivity::class.java)
            intent.putExtra("index",0)
            startActivity(intent)
        }

    }

    inner class ToVipHandler {

        @JavascriptInterface
        fun postMessage(){
            val intent = Intent(requireContext(), ToBeVipOrPartnerActivity::class.java)
            startActivity(intent)
            StringUtils.log("???????????????VIP")
        }

    }

    inner class ShangXueYuanHandler {

        @JavascriptInterface
        fun postMessage(str: String?){
            //StringUtils.log("????????????video")
            Constants.isMainPageNeedToChangePage = true
            Constants.pageNeedToBeChange = 1
        }

    }

    inner class VideoHandler {

        @JavascriptInterface
        fun postMessage(str: String?){
            Constants.isMainPageNeedToChangePage = true
            Constants.pageNeedToBeChange = 0
            /*val bean : MainActivity.ChangeBean = MainActivity.ChangeBean()
            bean.pageIndex = 0
            EventBus.getDefault().post(bean)*/
        }

    }

    inner class NewWenBanTongHandler {

        @JavascriptInterface
        fun postMessage(str: String?) {
            if (isWenBanTongDownloaded()){
                openApp()
            } else {
                showAskDialog()
            }
        }

        val downloadurl = "https://api.equnshang.com/web/wbt.apk"

        fun showAskDialog(){
            MessageDialog.show(requireActivity() as AppCompatActivity,
                "???????????????",
                "????????????????????????????????????????????????",
                "??????","??????")
                .setOkButton(OnDialogButtonClickListener() { baseDialog, v ->
                    if (PermissionUtil.checkStoragePermission(requireActivity())){
                        baseDialog.doDismiss()
                        download()
                    } else {
                        val rxPermissions = RxPermissions(requireActivity())
                        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe { granted ->
                                if (granted) {
                                    baseDialog.doDismiss()
                                    download()
                                } else {
                                    DialogUtil.toast(requireContext(),"??????????????????????????????????????????")
                                    PermissionUtil.openPermissonInSetting(requireActivity())
                                }
                            }
                        return@OnDialogButtonClickListener false
                    }
                    false
                })
                .cancelable = false
        }

        fun download(){
            val path = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + File.separator + System.currentTimeMillis() + ".apk"
            FileDownloader.getImpl()
                .create(downloadurl)
                .setPath(path)
                .setListener(object : FileDownloadListener() {
                    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                    }
                    override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                        WaitDialog.show(requireActivity() as AppCompatActivity,"????????????")
                    }

                    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        //WaitDialog.dismiss(0)
                        //StringUtils.log("???????????????" + (100*soFarBytes).toString())
                        val currrent : Long = soFarBytes.toLong() * 100
                        val total = totalBytes.toLong()
                        /*DialogUtil.refreshUpdateDialog((currrent/total).toInt())*/
                        //DialogUtil.showUpdateDialog(this@MainActivity).update()
                        WaitDialog.show(requireActivity() as AppCompatActivity,"????????????").setMessage("????????????" + (currrent/total) + "%")
                            .refreshView()
                    }
                    override fun blockComplete(task: BaseDownloadTask?) {

                    }
                    override fun retry(task: BaseDownloadTask?, ex: Throwable, retryingTimes: Int, soFarBytes: Int) {

                    }

                    override fun completed(task: BaseDownloadTask?) {
                        WaitDialog.dismiss()
                        //DialogUtil.dismissDialog()
                        CommonUtil.installAPK(path,requireContext())
                    }
                    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                    }
                    override fun error(task: BaseDownloadTask?, e: Throwable?) {
                        //DialogUtil.toast(this@MainActivity,e.message)
                    }
                    override fun warn(task: BaseDownloadTask?) {

                    }
                }).start()
        }

        fun isWenBanTongDownloaded() : Boolean{
            val packageManager = requireActivity().packageManager
            val pinfo : List<PackageInfo> ? = packageManager.getInstalledPackages(0)
            if (pinfo != null) {
                for (i in pinfo.indices) {
                    val pn = pinfo.get(i).packageName
                    if (pn.equals("com.hundsun.szwjs.pro")) {
                        return true
                    }
                }
            }
            return false
        }

        fun openApp(){
            if (!isWenBanTongDownloaded()){
                DialogUtil.toast(requireContext(),"?????????????????????")
                return
            }
            val lan = requireActivity().packageManager.getLaunchIntentForPackage("com.hundsun.szwjs.pro")
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = lan!!.component
            startActivity(intent)
        }

    }

    inner class LoginHandler {
        @JavascriptInterface //??????
        fun postMessage(str : String?) {
            if (StringUtils.isEmpty(UserInfoViewModel.getUserId())){
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    inner class NewsHandler {
        @JavascriptInterface
        fun postMessage(str : String?) {
            /*StringUtils.log("ajkfjdkfjkdjfkjdkgfd")
            val activity = this@WebViewActivity
            if (!PermissionUtil.checkStoragePermission(activity)){
                StringUtils.log("kfkfkffkfkkfkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk")
                PermissionUtil.requireStoragePermission(activity)
                return
            }*/
            /*StringUtils.log("" + str)
            val gson = Gson()
            val bean = gson.fromJson(str, CollegeNewsShareBean::class.java)
            val dialog = CollegeShareDialog(bean,requireContext(),url,intent.getStringExtra("shareimage"),intent.getStringExtra("bigtitle"),intent.getStringExtra("subtitle"))
            XPopup.Builder(this@WebViewActivity)
                .moveUpToKeyboard(false) //????????????????????????????????????????????????????????????
                .enableDrag(true)
                .isDestroyOnDismiss(false) //???????????????????????????????????????????????????
                //                        .isThreeDrag(true) //???????????????????????????????????????enableDrag(false)?????????
                .asCustom(dialog)
                .show()*/
            return

        }

    }

    inner class NewPeopleHandler{
        @JavascriptInterface //??????
        fun postMessage() {
            if (!StringUtils.isEmpty(UserInfoViewModel.getUserId())){
                val intent = Intent(requireContext(), WenBanTongAuthActivity::class.java)
                startActivityForResult(intent,1333)
                StringUtils.log("data")
            }
            //do your something
        }
    }

    inner class MyHandler{
        @JavascriptInterface //??????
        fun postMessage(data : String?) {
            val json = JSONObject(data)
            val productId = json.getInt("productId")
            val intent = Intent(requireContext(), AMallV3ProductDetailActivity::class.java)
            intent.putExtra("productId", productId)
            startActivity(intent)
            StringUtils.log(data + "?????????ID df ")
            //do your something
        }
    }

}