package com.qunshang.wenpaitong.equnshang.fragment

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

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
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_recommend_video.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import retrofit2.Call
import retrofit2.Callback
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.adapter.RecommendVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.utils.*
import com.qunshang.wenpaitong.equnshang.view.*
import com.qunshang.wenpaitong.equnshang.view.video.MyVideoController
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.player.VideoView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class SampleVideoFragment(var type : Int) : BaseVideoFragment() {

    private var adapter: RecommendVideoAdapter? = null

    private var dataBeanList: ArrayList<RecommendVideoBean.DataBean>? = null   //视频播放列表

    private var playedVideo: RecommendVideoBean.DataBean? = null                 //当前正在播放的视频

    private var publishId: String = ""      //当前视频发布者id

    private val apiVideoTest: ApiVideo = ApiManager.getInstance().getApiVideoTest()

    val TYPE_QQ : Int? = 589

    val TYPE_WECHAT = 652

    var downloadtype : Int? = TYPE_QQ

    //跳转到QQ或者微信
    fun goToApp(){
        if (downloadtype == TYPE_WECHAT){
            openWeChat()
        } else if (downloadtype == TYPE_QQ){
            openQQ()
        }
    }

    override fun setLayoutId(): Int {
        return R.layout.fragment_recommend_video
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    lateinit var wrapper: ControlWrapper

    lateinit var controller : MyVideoController

    var isAttached = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAttached = true
    }

    fun startProgress(controller : MyVideoController){
        if (!isAttached){
            startProgress(controller)
            return
        }
        val timer = Timer()
        val timerTask = object : TimerTask(){
            override fun run() {
                if (!wrapper.isPlaying) {
                    return
                }
                if (isPaused){
                    //DialogUtil.toast(requireContext(),"卧槽快暂停啊")
                    mVideoView?.pause()
                    return
                }
                requireActivity().runOnUiThread {
                    //StringUtils.log("启动")
                    val pos: Int = setProgress()
                    if (this@SampleVideoFragment::wrapper.isInitialized){
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

    var mIsDragging = false

    fun setProgress(duration: Int, position: Int) {
        if (mIsDragging) {
            return
        }
        if (this::seekBar.isInitialized) {
            if (duration > 0) {
                seekBar.isEnabled = true
                val pos = (position * 1.0 / duration * seekBar.max).toInt()
                seekBar.progress = pos
                //seekBar.setProgress(pos)
            } else {
                seekBar.isEnabled = false
            }
            val percent: Int = wrapper.bufferedPercentage
            if (percent >= 95) { //解决缓冲进度不能100%问题
                seekBar.secondaryProgress = seekBar.max
                seekBar.secondaryProgress = seekBar.max
            } else {
                seekBar.secondaryProgress = percent * 10
                seekBar.secondaryProgress = percent * 10
            }
        }
    }

    private fun setProgress(): Int {
        val position = wrapper.currentPosition.toInt()
        val duration = wrapper.duration.toInt()
        setProgress(duration, position)
        return position
    }

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    override fun init(){

        dataBeanList = ArrayList()
        mVideoView = player
        mVideoView?.isClickable = false
        controller = MyVideoController(requireContext())
        mVideoView?.setVideoController(controller) //设置控制器
        wrapper = controller.mControlWrapper

        startProgress(controller)

        val managerFansA = LinearLayoutManager(requireContext())
        managerFansA.orientation = LinearLayoutManager.VERTICAL
        list?.layoutManager = managerFansA
        list?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFansAAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerFansA.childCount
                    var totalItemCount = managerFansA.itemCount
                    //StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isFansAloading && lastFansAVisibleItemPosition == totalItemCount - 1) {
                        isFansAloading = true
                        loadVideo(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansAVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                //StringUtils.log("最后可见" + lastFansAVisibleItemPosition)
            }
        })
        adapter = RecommendVideoAdapter(requireContext(),ArrayList<RecommendVideoBean.DataBean>())
        list?.adapter = adapter

        mVideoView?.setLooping(true)

        TimeUtil.startComputeTimer()
        TimeUtil.pauseCompute()

        mVideoView?.setOnStateChangeListener(object : VideoView.OnStateChangeListener{
            override fun onPlayerStateChanged(playerState: Int) {
                //Log.i(Constants.logtag,"正在播放")
                try {
                    if (activity == null){
                        return
                    }
                    requireActivity()?.runOnUiThread {
                        if (parentFragment == null){
                            return@runOnUiThread
                        }
                        if (requireParentFragment().isHidden){
                            mVideoView?.pause()
                        }
                    }
                } catch (e : Exception){
                    e.printStackTrace()
                }
                //StringUtils.log("playerState" + playerState)
            }

            override fun onPlayStateChanged(playState: Int) {
                try {
                    if (activity == null){
                        return
                    }
                    requireActivity()?.runOnUiThread {
                        if (playState == VideoView.STATE_PLAYING){
                            Log.i(Constants.logtag,"正在播放")
                            if (parentFragment == null){

                            }
                            if (requireParentFragment().isHidden){
                                mVideoView?.pause()
                            }

                        }
                        if (playState == VideoView.STATE_PLAYING){
                            ivPlay?.visibility = View.GONE
                            TimeUtil.startComputeTimer()
                        }
                        if (playState == VideoView.STATE_PAUSED){
                            ivPlay?.visibility = View.VISIBLE
                            TimeUtil.pauseCompute()
                        }
                    }
                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
        })
        loadVideo()
        setViewPagerLayoutManager()
        setRefreshEvent()
    }

    fun showShareDialog(){
        val dialog = MainShareDialog(this)
        XPopup.Builder(requireContext())
            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
            .enableDrag(true)
            .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
            //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
            .asCustom(dialog)
            .show()
    }

    fun openWeChat(){
        if (!isWeChatClientAvailable(requireContext())){
            DialogUtil.toast(requireContext(),"请先安装微信")
            return
        }
        val lan = requireContext().packageManager.getLaunchIntentForPackage("com.tencent.mm")
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = lan!!.component
        startActivity(intent)
    }

    fun openQQ(){
        if (!isQQClientAvailable(requireActivity())){
            DialogUtil.toast(requireActivity(),"请先安装QQ")
            return
        }
        val lan = requireActivity().packageManager.getLaunchIntentForPackage("com.tencent.mobileqq")
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = lan!!.component
        startActivity(intent)
    }

    fun isWeChatClientAvailable(context : Context) : Boolean {
        val packageManager = context.packageManager
        val pinfo : List<PackageInfo> ? = packageManager.getInstalledPackages(0)
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo.get(i).packageName
                if (pn.equals("com.tencent.mm")) {
                    return true
                }
            }
        }
        return false
    }

    fun isQQClientAvailable(context : Context) : Boolean {
        val packageManager = context.packageManager
        val pinfo : List<PackageInfo> ? = packageManager.getInstalledPackages(0)
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo.get(i).packageName
                if (pn.equals("com.tencent.mobileqq")) {
                    return true
                }
            }
        }
        return false
    }

    fun showSuccessDialog(){
        TipDialog.show(requireActivity() as AppCompatActivity,"保存成功!", TipDialog.TYPE.SUCCESS)
    }

    fun showGetPasswordSuccessDialog(){
        TipDialog.show(requireActivity() as AppCompatActivity,"生成口令成功!", TipDialog.TYPE.SUCCESS)
    }

    fun showQQShareDialog(){
        val list = ArrayList<CharSequence>()
        BottomMenu.show(activity as AppCompatActivity,
            list,object : OnMenuItemClickListener{
                override fun onClick(text: String?, index: Int) {
                }
            }).setCustomView(R.layout.layout_qq_share, object : BottomMenu.OnBindView {

            override fun onBind(bottomMenu: BottomMenu?, v: View?) {
                this@SampleVideoFragment.downloadtype = TYPE_QQ
                val layout_video = v!!.findViewById<View>(R.id.layout_send_qq)
                layout_video.setOnClickListener {
                    downloadVideo()
                    bottomMenu?.doDismiss()
                }
            }
        })
    }

    fun loadVideoPassword(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ApiManager.getInstance().getApiVideoTest().loadVideoPassword(UserInfoViewModel.getUserId(),playedVideo!!.videoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<VideoPasswordBean>{

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onNext(t: VideoPasswordBean?) {
                        showGetPasswordSuccessDialog()
                        copyToClipboard(t?.data!!.password)
                        goToApp()
                    }

                    override fun onError(e: Throwable?) {
                        DialogUtil.toast(requireActivity(),"出错了")
                        //goToApp()
                    }

                    override fun onComplete() {

                    }

                })
        }
    }

    fun copyToClipboard(str : String){
        val cmb: ClipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.text = str
    }

    fun showWeChatShareDialog(){
        val list = ArrayList<CharSequence>()
        BottomMenu.show(activity as AppCompatActivity,
             list,object : OnMenuItemClickListener{
            override fun onClick(text: String?, index: Int) {
            }
        }).setCustomView(R.layout.layout_wechat_share, object : BottomMenu.OnBindView {

                override fun onBind(bottomMenu: BottomMenu?, v: View?) {
                    this@SampleVideoFragment.downloadtype = TYPE_WECHAT
                    val layout_video = v!!.findViewById<View>(R.id.layout_send_wechat)
                    //val layout_message = v.findViewById<View>(R.id.sendmessagetowechat)
                    layout_video.setOnClickListener {
                        if (PermissionUtil.checkStoragePermission(requireActivity())){
                            downloadVideo()
                        } else {
                            val rxPermissions = RxPermissions(requireActivity())
                            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .subscribe { granted ->
                                        if (granted) {
                                            downloadVideo()
                                        } else {
                                            DialogUtil.toast(requireContext(),"存储权限被拒绝，无法下载")
                                            PermissionUtil.openPermissonInSetting(requireActivity())
                                        }
                                    }
                        }
                        bottomMenu?.doDismiss()
                    }
                    /*layout_message.setOnClickListener {
                        bottomMenu?.doDismiss()
                        loadVideoPassword()
                    }*/
                }
            })
    }

    fun downloadVideo(){
        //Aria.download(this).register()
        if (!PermissionUtil.checkStoragePermission(requireActivity())){
            PermissionUtil.requireStoragePermission(requireActivity())
            return
        }
        val filePath = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/QunShang/" + System.currentTimeMillis() + ".mp4"
        //val filePath = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + File.separator + System.currentTimeMillis() + ".mp4"
        //val filePath: String = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/QunShang/" + System.currentTimeMillis() + ".mp4" // 保存路径
        //StringUtils.log(filePath)
        FileDownloader.getImpl().create(playedVideo!!.videoUrl)
            .setPath(filePath)
            .setListener(object : FileDownloadListener(){
                override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }

                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    //StringUtils.log("当前进度是" + (100*soFarBytes).toString())
                    var currrent : Long = soFarBytes.toLong() * 100
                    var total = totalBytes.toLong()
                    WaitDialog.show(requireActivity() as AppCompatActivity,"正在下载")
                        .setMessage("正在下载" + (currrent/total) + "%")
                        .refreshView()
                }

                override fun completed(task: BaseDownloadTask?) {
                    FileUtils.saveVideo(requireContext(), File(filePath))//通知相册
                    WaitDialog.dismiss()
                    showSuccessDialog()
                    goToApp()
                    //CommonUtil.installAPK(filePath,requireActivity() as AppCompatActivity)
                }

                override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

                }

                override fun error(task: BaseDownloadTask?, e: Throwable?) {
                    //DialogUtil.toast(requireContext(),e?.message)
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
                    WaitDialog.show(requireActivity() as AppCompatActivity,"正在下载")
                }

            }).start()
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        if (!isDataLoaded){
            init()
            isDataLoaded = true
        } else {
            if (type == VideoType.TYPE_RECOMMEND){
                if (MainFragment.curPage == 0 && MainActivity.index == 0) {
                    mVideoView?.start()
                }
            } else if (type == VideoType.TYPE_PIONEER){
                if (MainFragment.curPage == 0 && MainActivity.index == 2) {
                    mVideoView?.start()
                }
            }
        }

    }

    var isPaused = false

    override fun onPause() {
        super.onPause()
        isPaused = true
        mVideoView?.pause()
    }
    override fun onStop() {
        super.onStop()
        mVideoView?.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        mVideoView?.release()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(str : String){
        if ("loginsuccess".equals(str)){
            try {
                if (adapter != null){
                    adapter?.clearDatas()
                }
                pageIndex = 1
            } catch (e : java.lang.Exception){
                e.printStackTrace()
            }
            //init()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(bean: FollowChangeBean) {
        if (dataBeanList == null){
            return
        }
        if (adapter == null){
            return
        }
        for (i in adapter!!.datas.indices) {
            if (adapter!!.datas.get(i).agencyId == bean.id) {
                if (bean.isFollow) {
                    adapter!!.datas.get(i).isFocus = 0
                } else {
                    adapter!!.datas.get(i).isFocus = 222
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

    lateinit var controllerView: RecommendControllerView

    lateinit var seekBar : SeekBar

    lateinit var ivUp : ImageView

    override fun playCurVideo(position: Int) {
        //DialogUtil.toast(requireContext(),"iii" + position)
        val count: Int = mVideoView!!.childCount
        for (i in 0 until count) {
            if (position == curPlayPos) {
                return
            }
            if (ivCover != null){
                ivCover?.visibility = View.VISIBLE
                //StringUtils.log("啊啊")
            }
            val itemView = viewPagerLayoutManager!!.findViewByPosition(position) ?: return
            val rootView = itemView.findViewById<ViewGroup>(R.id.rl_item_video_container)
            val likeView: LikeView = rootView.findViewById(R.id.lv_item_video_likeview)
            val ivFocus : View = rootView.findViewById(R.id.ivFocus)

            ivUp = rootView.findViewById(R.id.ivUp)

            seekBar = rootView.findViewById(R.id.seekbar)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val duration: Long = wrapper.duration
                    val newPosition: Long = duration * progress / seekBar!!.max
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    mIsDragging = true
                    wrapper.stopProgress()
                    wrapper.stopFadeOut()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val duration: Long = wrapper.duration
                    val newPosition: Long = duration * seekBar!!.progress / this@SampleVideoFragment.seekBar.max
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
                            seekBar.maxHeight = CommonUtil.dp2px(requireContext(),10)
                            seekBar.minHeight = CommonUtil.dp2px(requireContext(),10)
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
                            seekBar.maxHeight = CommonUtil.dp2px(requireContext(),2)
                            seekBar.minHeight = CommonUtil.dp2px(requireContext(),2)
                        }

                    }
                }
                return@setOnTouchListener false
            }
            ivPlay = rootView.findViewById(R.id.ivplay)
            ivCover = rootView.findViewById<ImageView>(R.id.iv_item_video_cover)
            ivPlay?.visibility = View.GONE
            ivPlay?.alpha = 0.4f

            //播放暂停事件
            likeView.setOnClickListener {
                if (mVideoView!!.isPlaying) {
                    mVideoView?.pause()
                    ivPlay?.visibility = View.VISIBLE
                } else {
                    mVideoView?.start()
                    ivPlay?.visibility = View.GONE
                }
            }

            curPlayPos = position

            //这个地方被我注释掉了，导致了严重的bug ，提昂天该地

            detachParentView(rootView)//切换播放器位置

            autoPlayVideo(position, ivCover)

            if (playedVideo?.isFocus != 0) {
                ivFocus.visibility = View.GONE
                val gson = Gson()
                var toJson = gson.toJson(playedVideo)
                //StringUtils.log("当前的json是" + toJson)
            } else {
                ivFocus.visibility = View.VISIBLE
            }

            likeShareEvent(controllerView) //评论点赞事件
        }
    }


    /**
     * 自动播放视频
     */
    private fun autoPlayVideo(position: Int, ivCover: ImageView?) {
        playedVideo = dataBeanList?.get(position)   //当前正在播放的视频资源
        if (position == (dataBeanList!!.size - 1)){
            DialogUtil.toast(requireContext(),"已经是最后一个视频了")
        }
        mVideoView?.release()
        mVideoView?.setUrl(playedVideo!!.videoUrl)      //加载视频
        if (!isPaused){
            mVideoView?.start()
        }
        //延迟取消封面，避免加载视频黑屏
        object : CountDownTimer(200, 200) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                ivCover?.visibility = View.GONE
                ivCurCover = ivCover
            }
        }.start()
        videoId = playedVideo!!.videoId
        commentTitle = playedVideo!!.commentCount.toString()
        publishId = playedVideo!!.agencyId.toString()

    }

    /**
     * 加载视频播放列表
     */
    private fun loadVideo(isShowDialog : Boolean = true){
        if (isShowDialog){
            WaitDialog.show(requireActivity() as AppCompatActivity,"正在获取视频")
        }
        val callback = object: Callback<RecommendVideoBean>{
            override fun onResponse(call: Call<RecommendVideoBean>, response: retrofit2.Response<RecommendVideoBean>) {
                WaitDialog.dismiss()
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data == null){
                    return
                }
                val gson = Gson()
                //StringUtils.log("ajkjzkjfkdjfkdf" + response.body()!!.data.size + "  " + gson.toJson(response.body()))
                /*dataBeanList?.addAll(response.body()!!.data)
                if (adapter != null){
                    adapter?.addDatas(response.body()!!.data)
                }*/
                /*adapter = RecommendVideoAdapter(
                    activity,
                    dataBeanList
                )*/
                //list.adapter = adapter
                isDataLoaded = true
                list?.background = null

                isFansAloading = false
                dataBeanList?.addAll(response.body()!!.data)
                if (adapter != null){
                    adapter?.addDatas(response.body()!!.data)
                }
                pageIndex++
                if (response.body()!!.data.size < pageSize){
                    isFansAAutoLoadMoreEnabled = false
                    //StringUtils.log("停止了，这个被禁用了")
                }

            }

            override fun onFailure(call: Call<RecommendVideoBean>, t: Throwable) {
                WaitDialog.dismiss()
                t.message
            }
        }

        if (this.type == VideoType.TYPE_RECOMMEND){
            //StringUtils.log("current userId is " + UserInfoViewModel.getUserId())
            apiVideoTest.loadVideoList(UserInfoViewModel.getUserId(),pageIndex = pageIndex,pageSize = pageSize).enqueue(callback)
        } else if (this.type == VideoType.TYPE_PIONEER){
            list?.background = null
            apiVideoTest.loadPioneerVideo(UserInfoViewModel.getUserId()).enqueue(callback)
        }

    }

    var pageIndex = 1

    val pageSize = 5

    /**
     * 用户操作事件
     */
    private fun likeShareEvent(controllerView: RecommendControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {

            override fun onHeadClick() {

            }

            override fun onUpClick() {
                if (playedVideo!!.isUp == 0) {
                    ivUp?.isClickable = false

                    val upCount = playedVideo!!.upCount + 1
                    val tvUpCount = controllerView.findViewById<TextView>(R.id.tvUpCount)
                    tvUpCount?.text = upCount.toString()
                    Glide.with(requireContext()).load(R.mipmap.btn_main_up_true).into(ivUp)
                    ivUp?.setImageResource(R.mipmap.btn_main_up_true)
                    ivUp?.isClickable = true
                    playedVideo!!.isUp = 1000
                    playedVideo!!.upCount = upCount

                    apiVideoTest.upVideo(UserInfoViewModel.getUserId(), videoId, publishId)
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<UpLikeBean>, response: retrofit2.Response<UpLikeBean>) {

                            }

                            override fun onFailure(call: Call<UpLikeBean>, t: Throwable) {
                            }
                        })
                }
            }

            override fun onLikeClick() {
                apiVideoTest.likeVideo(UserInfoViewModel.getUserId(), videoId, publishId)
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

            override fun onCommentClick() {
                val dialog = CommentDialogV2(requireContext(),videoId,playedVideo?.agencyId.toString())
                XPopup.Builder(requireContext())
                        .isViewMode(true)
                    .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                    .enableDrag(true)
                    .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                    //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                    .setPopupCallback(object : XPopupCallback{
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
                    .asCustom(dialog)

                    .show()
            }

            /*点击【邀请码】*/
            override fun onShareClick() {
                showShareDialog()
            }
        })
    }

}