package com.qunshang.wenpaitong.equnshang.activity

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kongzue.dialog.interfaces.OnMenuItemClickListener
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.interfaces.OnViewPagerListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.view.*
import kotlinx.android.synthetic.main.activity_video_list.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import com.qunshang.wenpaitong.equnshang.adapter.RecommendVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean
import com.qunshang.wenpaitong.equnshang.data.UpLikeBean
import com.qunshang.wenpaitong.equnshang.data.VideoPasswordBean
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.fragment.BaseVideoFragment
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.video.MyVideoController
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.player.VideoView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class VideoListActivity : BaseActivity() {

    var data : List<RecommendVideoBean.DataBean>? = null

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
        //Aria.download(this).register()
        toolbar_back.setOnClickListener { finish() }
        //this.data = intent.getSerializableExtra("data") as List<RecommendVideoBean.DataBean>
        this.data = VideoDataUtil.getVideoData()
        this.position = intent.getIntExtra("position",0)
        if (data == null){
            return
        }
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
        VideoDataUtil.clear()
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

    private var adapter: RecommendVideoAdapter? = null

    private var playedVideo: RecommendVideoBean.DataBean? = null
    private val userId: String = UserInfoViewModel.getUserId()
    private var publishId: String = ""

    private val apiVideoTest: ApiVideo? = ApiManager.getInstance().getApiVideoTest()


    fun init(){
        mVideoView = player

        mVideoView?.isClickable = false
        controller = MyVideoController(this)
        mVideoView?.setVideoController(controller) //设置控制器
        wrapper = controller.mControlWrapper

        startProgress(controller)

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
                if (isFirst){
                    playCurVideo(position)
                    isFirst = false
                }
            }

        })
    }

    var isFirst = true

    override fun onPause() {
        super.onPause()
        mVideoView?.pause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(bean: FollowChangeBean) {
        if (data?.size == 0){
            return
        }
        if (adapter == null){
            return
        }
        for (i in adapter!!.datas.indices) {
            if (adapter!!.datas.get(i).agencyId == bean.id) {
                if (bean.isFollow) {
                    adapter!!.datas.get(i).setIsFocus(0)
                } else {
                    adapter!!.datas.get(i).setIsFocus(222)
                }
                if (playedVideo != null){
                    if (bean.id == playedVideo!!.agencyId){
                        if (this::controllerView.isInitialized){
                            controllerView.followStatusChanged(bean)
                        }
                    }
                }
            }
        }
    }

    lateinit var controllerView : RecommendControllerView

    lateinit var seekBar : SeekBar

    var mIsDragging = false

    lateinit var wrapper: ControlWrapper

    lateinit var controller : MyVideoController

    private fun setProgress(): Int {
        val position = wrapper.getCurrentPosition().toInt()
        val duration = wrapper.getDuration().toInt()
        setProgress(duration, position)
        return position
    }

    fun setProgress(duration: Int, position: Int) {
        if (mIsDragging) {
            return
        }
        if (this::seekBar.isInitialized) {
            if (duration > 0) {
                seekBar.setEnabled(true)
                val pos = (position * 1.0 / duration * seekBar.getMax()).toInt()
                seekBar.setProgress(pos)
                //seekBar.setProgress(pos)
            } else {
                seekBar.setEnabled(false)
            }
            val percent: Int = wrapper.getBufferedPercentage()
            if (percent >= 95) { //解决缓冲进度不能100%问题
                seekBar.setSecondaryProgress(seekBar.getMax())
                seekBar.setSecondaryProgress(seekBar.getMax())
            } else {
                seekBar.setSecondaryProgress(percent * 10)
                seekBar.setSecondaryProgress(percent * 10)
            }
        }
    }

    fun startProgress(controller : MyVideoController){
        val timer = Timer()
        val timerTask = object : TimerTask(){
            override fun run() {
                if (!wrapper.isPlaying()) {
                    return
                }
                runOnUiThread {
                    //StringUtils.log("启动")
                    val pos: Int = setProgress()
                    if (this@VideoListActivity::wrapper.isInitialized){
                        //StringUtils.log("总" + wrapper.duration)
                        //StringUtils.log("目前" + wrapper.currentPosition)
                        if (wrapper.duration/1000 == wrapper.currentPosition/1000 + 1){
                            //StringUtils.log("视频看娃了")
                            CommonUtil.doCompleteTask(1)
                        }
                    }
                }
            }

        }
        timer.schedule(timerTask,0,1000)
    }

    fun playCurVideo(position: Int = 0) {
        val count: Int = mVideoView!!.childCount
        for (i in 0 until count) {
            if (position == curPlayPos) {
                StringUtils.log("pos是" + position)     //position就没有变过，
                StringUtils.log("curplashi" + curPlayPos)
                return
            }
            StringUtils.log("foufoupos是" + position)
            StringUtils.log("foufoucurplashi" + curPlayPos)
            StringUtils.log("我经过了")
            if (ivCover != null){
                ivCover?.visibility = View.VISIBLE
                //StringUtils.log("上一个封面显示了")
            }
            val itemView = viewPagerLayoutManager!!.findViewByPosition(position) ?: return
            //com.qunshang.wenpaitong.equnshang.utils.ToastUtil.toast(this,"调用1")
            val rootView = itemView.findViewById<ViewGroup>(R.id.rl_item_video_container)
            val likeView: LikeView = rootView.findViewById(R.id.lv_item_video_likeview)
            val ivFocus : View = rootView.findViewById(R.id.ivFocus)


            seekBar = rootView.findViewById(R.id.seekbar)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val duration: Long = wrapper.getDuration()
                    val newPosition: Long = duration * progress / seekBar!!.getMax()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    mIsDragging = true
                    wrapper.stopProgress()
                    wrapper.stopFadeOut()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val duration: Long = wrapper.getDuration()
                    val newPosition: Long = duration * seekBar!!.progress / this@VideoListActivity.seekBar.getMax()
                    wrapper.seekTo(newPosition)
                    mIsDragging = false
                    wrapper.startProgress()
                    wrapper.startFadeOut()
                }

            })

            controllerView = rootView.findViewById(R.id.cv_item_video_controller)
            seekBar.setOnTouchListener { view, motionEvent ->
                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            seekBar.maxHeight = CommonUtil.dp2px(this,10)
                            seekBar.minHeight = CommonUtil.dp2px(this,10)
                        }
                        /*val anim = ValueAnimator.ofInt(CommonUtil.dp2px(requireContext(),2),CommonUtil.dp2px(requireContext(),10))
                        anim.addUpdateListener {
                            val current = it.getAnimatedValue() as Int
                            seekBar.maxHeight = current
                            seekBar.minHeight = current
                        }
                        anim.setDuration(2000).start()*/
                    }
                    MotionEvent.ACTION_UP -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            seekBar.maxHeight = CommonUtil.dp2px(this,2)
                            seekBar.minHeight = CommonUtil.dp2px(this,2)
                        }

                    }
                }
                return@setOnTouchListener false
            }
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
            if (playedVideo?.isFocus != 0) {
                ivFocus.visibility = RelativeLayout.GONE
            } else {
                ivFocus.visibility = RelativeLayout.VISIBLE
            }

            likeShareEvent(controllerView)
        }
    }

    var ivCover : ImageView ? = null
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
                //StringUtils.log("新的封面隐藏了")
                ivCurCover = ivCover
            }
        }.start()
        videoId = playedVideo!!.videoId
        BaseVideoFragment.commentTitle = playedVideo!!.commentCount.toString()
        publishId = playedVideo!!.agencyId.toString()


    }

    private fun loadVideo(){

        adapter = RecommendVideoAdapter(
            this@VideoListActivity,
            data
        )
        list.adapter = adapter
        isDataLoaded = true
    }

    /**
     * 用户操作事件
     */
    private fun likeShareEvent(controllerView: RecommendControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            /*点击【头像】*/
            override fun onHeadClick() {
            }


            override fun onUpClick() {
                if (playedVideo!!.isUp == 0) {

                    val upCount = playedVideo!!.upCount + 1
                    tvUpCount.text = upCount.toString()
                    ivUp.setImageResource(R.mipmap.btn_main_up_true)
                    playedVideo!!.isUp = 1000
                    playedVideo!!.upCount = upCount

                    apiVideoTest!!.upVideo(userId, videoId, publishId)
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<UpLikeBean>, response: retrofit2.Response<UpLikeBean>) {

                            }

                            override fun onFailure(call: Call<UpLikeBean>, t: Throwable) {
                            }
                        })
                }
            }

            /*点击【爱心】*/
            override fun onLikeClick() {
                apiVideoTest!!.likeVideo(userId, videoId, publishId)
                    .enqueue(object : Callback<UpLikeBean> {
                        override fun onResponse(
                            call: Call<UpLikeBean>,
                            response: retrofit2.Response<UpLikeBean>
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
                XPopup.Builder(this@VideoListActivity)
                        .isViewMode(true)
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
                    .asCustom(CommentDialogV2(this@VideoListActivity,videoId,playedVideo?.agencyId.toString()))
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
            list,object : OnMenuItemClickListener{
                override fun onClick(text: String?, index: Int) {
                }
            }).setCustomView(R.layout.layout_wechat_share, object : BottomMenu.OnBindView {

            override fun onBind(bottomMenu: BottomMenu?, v: View?) {
                this@VideoListActivity.downloadtype = TYPE_WECHAT
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
        val dialog = MainShareDialogWithActivity(this)
        XPopup.Builder(this)
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(dialog)
            .show()
        /*val itemList = ArrayList<ShareDialog.Item>()
        itemList.add(ShareDialog.Item(this ,R.mipmap.wechat,"微信"))
        itemList.add(ShareDialog.Item(this ,R.mipmap.friendcircle,"朋友圈"))
        itemList.add(ShareDialog.Item(this ,R.mipmap.qqfriend,"QQ好友"))
        itemList.add(ShareDialog.Item(this ,R.mipmap.qqzone,"QQ空间"))
        itemList.add(ShareDialog.Item(this ,R.mipmap.saveimage,"邀请码"))

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
                        this@VideoListActivity.downloadtype = TYPE_WECHAT
                        downloadVideo()
                    }
                    2 -> {
                        showQQShareDialog()
                    }
                    3 -> {
                        this@VideoListActivity.downloadtype = TYPE_QQ
                        downloadVideo()
                    }
                    4 -> {
                        //com.qunshang.wenpaitong.equnshang.view.ShareDialog().show(childFragmentManager, "")
                        val intent = Intent(this@VideoListActivity,QrCodeShareActivity::class.java)
                        startActivity(intent)
                    }
                }
                return false
            }
        })*/

    }

    fun showSuccessDialog(){
        TipDialog.show(this as AppCompatActivity,"保存成功!", TipDialog.TYPE.SUCCESS)
    }

    fun downloadVideo(){
        if (!PermissionUtil.checkStoragePermission(this)){
            PermissionUtil.requireStoragePermission(this)
            return
        }
        val filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + File.separator + System.currentTimeMillis() + ".mp4"
        FileDownloader.getImpl().create(playedVideo!!.videoUrl)
            .setPath(filePath)
            .setListener(object : FileDownloadListener(){
                override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }

                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    StringUtils.log("当前进度是" + (100*soFarBytes).toString())
                    var currrent : Long = soFarBytes.toLong() * 100
                    var total = totalBytes.toLong()
                    WaitDialog.show(this@VideoListActivity,"正在下载").setMessage("正在下载" + (currrent/total) + "%")
                        .refreshView()
                }

                override fun completed(task: BaseDownloadTask?) {
                    FileUtils.saveVideo(this@VideoListActivity, File(filePath))
                    WaitDialog.dismiss()
                    showSuccessDialog()
                    goToApp()
                }

                override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }

                override fun error(task: BaseDownloadTask?, e: Throwable?) {

                }

                override fun warn(task: BaseDownloadTask?) {

                }

                override fun connected(
                    task: BaseDownloadTask?,
                    etag: String?,
                    isContinue: Boolean,
                    soFarBytes: Int,
                    totalBytes: Int
                ) {
                    super.connected(task, etag, isContinue, soFarBytes, totalBytes)
                    WaitDialog.show(this@VideoListActivity,"正在下载")
                }

            }).start()
    }

    fun showQQShareDialog(){
        val list = ArrayList<CharSequence>()
        BottomMenu.show(this,
            list,object : OnMenuItemClickListener{
                override fun onClick(text: String?, index: Int) {
                }
            }).setCustomView(R.layout.layout_qq_share, object : BottomMenu.OnBindView {

            override fun onBind(bottomMenu: BottomMenu?, v: View?) {
                this@VideoListActivity.downloadtype = TYPE_QQ
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

                    override fun onNext(t: VideoPasswordBean?) {
                        showGetPasswordSuccessDialog()
                        copyToClipboard(t?.data!!.password)
                        goToApp()
                    }

                    override fun onError(e: Throwable?) {
                        DialogUtil.toast(this@VideoListActivity,"出错了")
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
        if (!isQQClientAvailable(this)){
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