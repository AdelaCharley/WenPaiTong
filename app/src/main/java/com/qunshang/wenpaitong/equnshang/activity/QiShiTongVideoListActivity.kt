package com.qunshang.wenpaitong.equnshang.activity;

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.ShareDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_list.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.QiShiTongVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.interfaces.OnViewPagerListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.FileUtils
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.view.*
import xyz.doikki.videoplayer.player.VideoView
import java.io.File

class QiShiTongVideoListActivity : BaseActivity() {

    lateinit var data : List<QiShiTongVideoBean.DataBean>

    var position = 0

    var isDataLoaded = false

    //var commentTitle: String = ""   //当前视频评论标题（“n条评论”）

    var mVideoView: VideoView<*>? = null

    var viewPagerLayoutManager: ViewPagerLayoutManager? = null

    var curPlayPos = -1 //当前播放视频的位置

    var ivCurCover: ImageView? = null

    var videoId = 0                 //当前视频id

    lateinit var refreshLayout : SwipeRefreshLayout

    fun start(){
        mVideoView?.start()
        if (this::ivPlay.isInitialized){
            ivPlay.visibility = View.GONE
        }
    }

    lateinit var ivPlay : ImageView

    lateinit var list : RecyclerView

    fun setViewPagerLayoutManager() {
        viewPagerLayoutManager = ViewPagerLayoutManager(this)
        list.layoutManager = viewPagerLayoutManager
        viewPagerLayoutManager!!.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {
                playCurVideo(0)
            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
                if (ivCurCover != null) {
                    ivCurCover!!.visibility = View.GONE
                }
            }

            override fun onPageSelected(position: Int, isBottom: Boolean) {
                playCurVideo(position)
            }
        })
    }

    fun detachParentView(rootView: ViewGroup) {

        if ((rootView == null) or (mVideoView == null)){
            return
        }
        mVideoView?.parent?.let {
            (it as ViewGroup).removeView(mVideoView)

        }
        rootView.addView(mVideoView, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_video_list)
        changeToDefaultButTranslucent()
        Aria.download(this).register()
        toolbar_back.setOnClickListener { finish() }
        this.data = intent.getSerializableExtra("data") as List<QiShiTongVideoBean.DataBean>
        this.position = intent.getIntExtra("position",0)
        list = findViewById(R.id.list)
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout.isEnabled = false
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView?.pause()
        mVideoView?.release()
        mVideoView = null
        EventBus.getDefault().unregister(this)
    }

    fun setRefreshEvent() {
        refreshLayout.setColorSchemeResources(R.color.color_link)
        refreshLayout.setOnRefreshListener {
            object : CountDownTimer(100, 100) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    refreshLayout.isRefreshing = false
                }
            }.start()
        }
    }

    private var adapter: com.qunshang.wenpaitong.equnshang.adapter.QiShiTongVideoAdapter? = null

    private var playedVideo: QiShiTongVideoBean.DataBean? = null
    private val userId: String = UserInfoViewModel.getUserId()
    private var publishId: String = ""

    private val apiVideoTest: ApiVideo? = ApiManager.getInstance().getApiVideoTest()


    fun init(){
        mVideoView = player
        mVideoView!!.setLooping(true)
        loadVideo()
        setViewPagerLayoutManager()
        setRefreshEvent()
        mVideoView?.setOnStateChangeListener(object : VideoView.OnStateChangeListener{
            override fun onPlayerStateChanged(playerState: Int) {
            }

            override fun onPlayStateChanged(playState: Int) {

                if (playState == VideoView.STATE_PLAYING){
                    TimeUtil.startComputeTimer()
                }
                if (playState == VideoView.STATE_PAUSED){
                    TimeUtil.pauseCompute()
                }
            }

        })
        list.scrollToPosition(position)
        list.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver
        .OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                playCurVideo(position)
            }

        })
    }

    override fun onPause() {
        super.onPause()
        mVideoView?.pause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(bean: ManufactureChangeBean) {
        if (data.size == 0){
            return
        }
        if (adapter == null){
            return
        }
        for (i in adapter!!.datas.indices) {
            if (adapter!!.datas.get(i).manufacturerId == bean.getId()) {
                if (bean.isFollow) {
                    adapter!!.datas.get(i).setIsFocus(0)
                } else {
                    adapter!!.datas.get(i).setIsFocus(222)
                }
                if (playedVideo != null){
                    if (bean.getId() == playedVideo!!.manufacturerId){
                        if (this::controllerView.isInitialized){
                            controllerView.followStatusChanged(bean)
                        }
                    }
                }
            }
        }
    }

    lateinit var controllerView : QiShiTongControllerView

    var ivCover : ImageView ? = null// rootView.findViewById<ImageView>(R.id.iv_item_video_cover)

    fun playCurVideo(position: Int = 0) {
        val count: Int = mVideoView!!.childCount
        for (i in 0 until count) {
            if (position == curPlayPos) {
                return
            }
            val itemView = viewPagerLayoutManager!!.findViewByPosition(position) ?: return
            //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this,"调用1")
            val rootView = itemView.findViewById<ViewGroup>(R.id.rl_item_video_container)
            val likeView: LikeView = rootView.findViewById(R.id.lv_item_video_likeview)
            controllerView = rootView.findViewById(R.id.cv_item_video_controller)
            controllerView.put("amall")
            ivPlay = rootView.findViewById<ImageView>(R.id.ivplay)
            ivCover = rootView.findViewById<ImageView>(R.id.iv_item_video_cover)
            ivPlay.visibility = View.GONE
            ivPlay.alpha = 0.4f
            //播放暂停事件
            likeView?.setOnClickListener {
                if (mVideoView!!.isPlaying) {
                    mVideoView?.pause()
                    ivPlay.visibility = View.VISIBLE
                } else {
                    mVideoView?.start()
                    ivPlay.visibility = View.GONE
                }
            }
            curPlayPos = position
            detachParentView(rootView)

            autoPlayVideo(position, ivCover)

            likeShareEvent(controllerView)
        }
    }


    /**
     * 自动播放视频
     */
    private fun autoPlayVideo(position: Int, ivCover: ImageView?) {
        //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this,"调用")
        playedVideo = data?.get(position)
        mVideoView!!.release()
        mVideoView!!.setUrl(playedVideo!!.videoUrl)      //加载视频
        mVideoView!!.start()
        //延迟取消封面，避免加载视频黑屏
        object : CountDownTimer(200, 200) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                ivCover?.visibility = View.GONE
                ivCurCover = ivCover
            }
        }.start()
        videoId = playedVideo!!.videoId
        BaseVideoFragment.commentTitle = playedVideo!!.commentCount.toString()
        publishId = playedVideo!!.manufacturerId.toString()


    }

    private fun loadVideo(){

        adapter = QiShiTongVideoAdapter(
            this,
            data
        )
        list.adapter = adapter
        isDataLoaded = true
    }

    /**
     * 用户操作事件
     */
    private fun likeShareEvent(controllerView: QiShiTongControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            /*点击【头像】*/
            override fun onHeadClick() {
            }


            override fun onUpClick() {
                if (playedVideo!!.isUp == 0) {
                    apiVideoTest!!.upManufactureVideo(userId, videoId.toString())
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<UpLikeBean>, response: retrofit2.Response<com.qunshang.wenpaitong.equnshang.data.UpLikeBean>) {
                                val upCount = playedVideo!!.upCount + 1
                                tvUpCount.text = upCount.toString()
                                ivUp.setImageResource(R.mipmap.btn_main_up_true)
                                playedVideo!!.isUp = 1000
                                playedVideo!!.upCount = upCount
                            }

                            override fun onFailure(call: Call<UpLikeBean>, t: Throwable) {
                            }
                        })
                }
            }

            /*点击【爱心】*/
            override fun onLikeClick() {
                apiVideoTest!!.likeManufactureVideo(userId, videoId.toString())
                    .enqueue(object : Callback<UpLikeBean> {
                        override fun onResponse(
                            call: Call<UpLikeBean>,
                            response: retrofit2.Response<com.qunshang.wenpaitong.equnshang.data.UpLikeBean>
                        ) {
                            val likeState = response.body()!!.data.statusCode
                            var likeCount = playedVideo!!.likeCount
                            when (likeState) {
                                0 -> {
                                    likeCount += 1
                                    cbLike.setImageResource(R.mipmap.btn_comment_like_true)
                                }
                                1 -> {
                                    likeCount -= 1
                                    cbLike.setImageResource(R.mipmap.btn_main_like_false)
                                }
                            }
                            tvLikeCount.text = likeCount.toString()
                            playedVideo!!.isLike = likeState
                            playedVideo!!.likeCount = likeCount
                        }

                        override fun onFailure(call: Call<UpLikeBean>, t: Throwable) {
                            t.message
                        }
                    })
            }

            /*点击【评论】*/
            override fun onCommentClick() {
                /*val commentDialog = CommentDialog(videoId)
                commentDialog.show(supportFragmentManager, "")*/
                XPopup.Builder(this@QiShiTongVideoListActivity)
                    .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                    .enableDrag(true)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                    .setPopupCallback(object : XPopupCallback {
                        override fun onCreated(popupView: BasePopupView?) {

                        }

                        override fun beforeShow(popupView: BasePopupView?) {

                        }

                        override fun onShow(popupView: BasePopupView?) {

                        }

                        override fun onDismiss(popupView: BasePopupView?) {
                            //ToastUtil.toast(requireContext(),"更新评论")
                            controllerView.update()//用户可能已经发生了评论，在此处更新评论内容
                        }

                        override fun beforeDismiss(popupView: BasePopupView?) {

                        }

                        override fun onBackPressed(popupView: BasePopupView?): Boolean {
                            return true
                        }

                        override fun onKeyBoardStateChanged(
                            popupView: BasePopupView?,
                            height: Int
                        ) {

                        }

                        override fun onDrag(
                            popupView: BasePopupView?,
                            value: Int,
                            percent: Float,
                            upOrLeft: Boolean
                        ) {

                        }

                    })
                    .asCustom(ManufactureCommentDialogV2(this@QiShiTongVideoListActivity,videoId,playedVideo?.manufacturerId.toString()))
                    .show()
            }

            /*点击【邀请码】*/
            override fun onShareClick() {
                showShareDialog()
                //ShareDialog().show(supportFragmentManager, "")
            }
        })
    }

    fun showWeChatShareDialog(){
        val list = ArrayList<CharSequence>()
        BottomMenu.show(this as AppCompatActivity,
            list,object : OnMenuItemClickListener {
                override fun onClick(text: String?, index: Int) {
                }
            }).setCustomView(R.layout.layout_wechat_share, object : BottomMenu.OnBindView {

            override fun onBind(bottomMenu: BottomMenu?, v: View?) {
                this@QiShiTongVideoListActivity.downloadtype = TYPE_WECHAT
                val layout_video = v!!.findViewById<View>(R.id.layout_send_wechat)
                //val layout_message = v.findViewById<View>(R.id.sendmessagetowechat)
                layout_video.setOnClickListener {
                    downloadVideo()
                    bottomMenu?.doDismiss()
                }
                /*layout_message.setOnClickListener {
                    bottomMenu?.doDismiss()
                    loadVideoPassword()
                }*/
            }
        })
    }

    fun showShareDialog(){
        val itemList = ArrayList<ShareDialog.Item>()
        itemList.add(ShareDialog.Item(this , R.mipmap.wechat,"微信"))
        itemList.add(ShareDialog.Item(this , R.mipmap.friendcircle,"朋友圈"))
        itemList.add(ShareDialog.Item(this , R.mipmap.qqfriend,"QQ好友"))
        itemList.add(ShareDialog.Item(this , R.mipmap.qqzone,"QQ空间"))
        itemList.add(ShareDialog.Item(this , R.mipmap.saveimage,"邀请码"))

        ShareDialog.show(this,itemList, object : ShareDialog.OnItemClickListener{
            override fun onClick(
                shareDialog: ShareDialog?,
                index: Int,
                item: ShareDialog.Item?
            ): Boolean {
                when(index){
                    0 -> {
                        showWeChatShareDialog()
                    }
                    1 -> {
                        this@QiShiTongVideoListActivity.downloadtype = TYPE_WECHAT
                        downloadVideo()
                    }
                    2 -> {
                        showQQShareDialog()
                    }
                    3 -> {
                        this@QiShiTongVideoListActivity.downloadtype = TYPE_QQ
                        downloadVideo()
                    }
                    4 -> {
                        //com.qunshang.wenpaitong.equnshang.view.ShareDialog().show(childFragmentManager, "")
                        val intent = Intent(this@QiShiTongVideoListActivity,QrCodeShareActivity::class.java)
                        startActivity(intent)
                    }
                }
                return false
            }
        })

    }

    @Download.onTaskPre fun running(task : DownloadTask) {
        showWaitDialog()
    }

    @Download.onTaskComplete fun taskComplete(task : DownloadTask) {
        FileUtils.saveVideo(this, File(task.filePath))
        WaitDialog.dismiss()
        showSuccessDialog()
        goToApp()
    }

    fun showWaitDialog(){
        WaitDialog.show(this as AppCompatActivity,"正在保存")
    }

    fun showSuccessDialog(){
        TipDialog.show(this as AppCompatActivity,"保存成功!", TipDialog.TYPE.SUCCESS)
    }

    fun downloadVideo(){
        val filePath: String = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera/" + System.currentTimeMillis() + ".mp4" // 保存路径
        val taskId: Long = Aria.download(this)
            .load(playedVideo!!.videoUrl) //读取下载地址
            .setFilePath(filePath) //设置文件保存的完整路径
            .create() //创建并启动下载
    }

    fun showQQShareDialog(){
        val list = ArrayList<CharSequence>()
        BottomMenu.show(this,
            list,object : OnMenuItemClickListener {
                override fun onClick(text: String?, index: Int) {
                }
            }).setCustomView(R.layout.layout_qq_share, object : BottomMenu.OnBindView {

            override fun onBind(bottomMenu: BottomMenu?, v: View?) {
                this@QiShiTongVideoListActivity.downloadtype = TYPE_QQ
                val layout_video = v!!.findViewById<View>(R.id.layout_send_qq)
                //val layout_message = v.findViewById<View>(R.id.sendmessagetoqq)
                layout_video.setOnClickListener {
                    downloadVideo()
                    bottomMenu?.doDismiss()
                }
                /*layout_message.setOnClickListener {
                    loadVideoPassword()
                    bottomMenu?.doDismiss()
                }*/
            }
        })
    }

    val TYPE_QQ = 589

    val TYPE_WECHAT = 652


    var downloadtype = TYPE_QQ

    fun showGetPasswordSuccessDialog(){
        TipDialog.show(this as AppCompatActivity,"生成口令成功!", TipDialog.TYPE.SUCCESS)
    }

    fun loadVideoPassword(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ApiManager.getInstance().getApiVideoTest().loadVideoPassword(UserInfoViewModel.getUserId(),playedVideo!!.videoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<VideoPasswordBean> {

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(t: com.qunshang.wenpaitong.equnshang.data.VideoPasswordBean?) {
                        showGetPasswordSuccessDialog()
                        copyToClipboard(t?.data!!.password)
                        goToApp()
                    }

                    override fun onError(e: Throwable?) {
                        DialogUtil.toast(this@QiShiTongVideoListActivity,"出错了")
                        //goToApp()
                    }

                    override fun onComplete() {

                    }

                })
        }
    }

    fun openWeChat(){
        if (!isWeChatClientAvailable(this)){
            DialogUtil.toast(this,"请先安装微信")
            return
        }
        val lan = this.packageManager.getLaunchIntentForPackage("com.tencent.mm")
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = lan!!.component
        startActivity(intent)
    }

    fun openQQ(){
        if (!isWeChatClientAvailable(this)){
            DialogUtil.toast(this,"请先安装QQ")
            return
        }
        val lan = this.packageManager.getLaunchIntentForPackage("com.tencent.mobileqq")
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = lan!!.component
        startActivity(intent)
    }

    fun isWeChatClientAvailable(context : Context) : Boolean {
        val packageManager = context.getPackageManager();
        val pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    fun isQQClientAvailable(context : Context) : Boolean {
        val packageManager = context.getPackageManager();
        val pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    fun copyToClipboard(str : String){
        val cmb: ClipboardManager =
            this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.setText(str)
    }

    //跳转到QQ或者微信
    fun goToApp() {
        if (downloadtype == TYPE_WECHAT) {
            openWeChat()
        } else if (downloadtype == TYPE_QQ) {
            openQQ()
        }
    }

}