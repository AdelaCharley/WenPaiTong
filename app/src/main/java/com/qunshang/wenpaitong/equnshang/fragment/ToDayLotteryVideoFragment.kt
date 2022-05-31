package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import kotlinx.android.synthetic.main.fragment_to_day_lottery_video.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import kotlinx.android.synthetic.main.view_controller_today_lottery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.doikki.videoplayer.player.AbstractPlayer
import xyz.doikki.videoplayer.player.VideoView
import java.lang.Exception
import java.util.*

import com.kongzue.dialog.v3.CustomDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import kotlinx.android.synthetic.main.activity_watch_video.*
import kotlinx.android.synthetic.main.dialog_today_laiduijiang.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.adapter.ToDayLotteryVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil.Companion.getDayOfWeek
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import com.qunshang.wenpaitong.equnshang.view.*

class ToDayLotteryVideoFragment(var type : Int) : BaseVideoFragment() {

    private var adapter: ToDayLotteryVideoAdapter? = null

    private var dataBeanList: MutableList<LotteryVideoBean.DataBean>? = null   //视频播放列表
    private var playedVideo: LotteryVideoBean.DataBean? = null                 //当前正在播放的视频

    //private val userId: String = UserInfoViewModel.getUserId()    //登录用户的id
    private var publishId: String = ""      //当前视频发布者id

    private val apiVideoTest: ApiVideo = ApiManager.getInstance().getApiVideoTest()
    private val TAG: String = "TEST_Recommend"

    override fun setLayoutId(): Int {
        return R.layout.fragment_to_day_lottery_video
    }

    override fun init() {
        mVideoView = VideoView<AbstractPlayer>(requireActivity().applicationContext)
        mVideoView = player
        mVideoView?.setLooping(true)
        //showReceiveTicketDialog() //这个测试用的，正式删掉
        setViewPagerLayoutManager()
        setRefreshEvent()
        mVideoView?.setOnStateChangeListener(object : VideoView.OnStateChangeListener{
            override fun onPlayerStateChanged(playerState: Int) {
                Log.i(Constants.logtag,"正在播放")
                if (parentFragment == null){
                    return
                }
            }

            override fun onPlayStateChanged(playState: Int) {

                if (playState == VideoView.STATE_PLAYING){
                    ivPlay?.visibility = View.GONE
                    TimeUtil.startComputeTimer()
                }
                if (playState == VideoView.STATE_PAUSED){
                    ivPlay?.visibility = View.VISIBLE
                    TimeUtil.pauseCompute()
                }
            }

        })
        loadVideo()
        //getWatchVideoStatus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        mVideoView?.release()
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
            if (adapter!!.datas.get(i).getUserId() == bean.id) {
                if (bean.isFollow) {
                    adapter!!.datas.get(i).setIsFocus(0)
                } else {
                    adapter!!.datas.get(i).setIsFocus(222)
                }
                if (playedVideo != null){
                    if (bean.id == playedVideo!!.userId){
                        if (this::controllerView.isInitialized){
                            controllerView.followStatusChanged(bean)
                        }
                    }
                }
            }
        }
    }

    var remain = 10

    val timer = Timer()
    val timerTask = object : TimerTask(){
        override fun run() {
            val time = (mVideoView!!.currentPosition)/1000
            remain = (10 - time).toInt()
            if (remain >= 0){
                activity?.runOnUiThread {
                    this@ToDayLotteryVideoFragment.time.setText(remain.toString() + "s")
                    progress?.setProgress(remain.toFloat() * 10,false)
                }
            }
            if (remain <= 0){
                activity?.runOnUiThread {
                    //DialogUtil.toast(context,"已经看完视频了")
                    //timer.cancel()
                    updateVideoStatus()
                }
            }
        }
    }

    //更新完观看视频的状态后才调用
    fun onVideoUpdated(){
        novideo.visibility = View.GONE
        progress.visibility = View.GONE
        progress_internal.visibility = View.GONE
        time.visibility = View.GONE
        timer.cancel()
        receive_ticket.visibility = View.VISIBLE
        receive_ticket.setOnClickListener {
            showReceiveTicketDialog()
        }
        showReceiveTicketDialog()
    }

    fun showReceiveTicketDialog(){
        CustomDialog.show(activity as AppCompatActivity, R.layout.dialog_receive_ticket,object : CustomDialog.OnBindView{
            override fun onBind(dialog: CustomDialog?, v: View?) {
                val image = v?.findViewById<ImageView>(R.id.image)
                val close = v?.findViewById<ImageView>(R.id.close)
                close?.setOnClickListener {
                    dialog?.doDismiss()
                }
                image?.setOnClickListener {
                    ApiManager.getInstance().getApiVideoTest().receiveTicket(UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>>{
                        override fun onResponse(
                            call: Call<BaseHttpBean<Int>>,
                            response: Response<BaseHttpBean<Int>>
                        ) {
                            if (response.body() == null){
                                return
                            }
                            dialog?.doDismiss()
                            //DialogUtil.toast(context,response.body()!!.msg)
                            if (response.body()!!.code == 200){
                                getWatchVideoStatus()
                                val intent = Intent(context,DailyLotteryTicketActivity::class.java)
                                startActivity(intent)
                                receive_ticket.visibility = View.GONE
                            }
                        }

                        override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                        }

                    })
                }

            }

        }).setCancelable(false)
    }

    //当视频观看状态为0时，未看视频
    fun whenStatusZero(){
        novideo?.visibility = View.VISIBLE
        progress?.visibility = View.VISIBLE
        progress_internal?.visibility = View.VISIBLE
        time.visibility = View.VISIBLE
        startCaculateVideoTime()
    }

    lateinit var statusbean : DailyTaskStatusBean.DataBean

    fun updateStatus(){
        if (!this::statusbean.isInitialized){
            return
        }
        when(statusbean.status){
            0 ->{
                whenStatusZero()
            }
            1 ->{
                receive_ticket?.visibility = View.VISIBLE
                novideo?.visibility = View.INVISIBLE
                progress?.visibility = View.GONE
                progress_internal?.visibility = View.GONE
                if (this::time.isInitialized){
                    time.visibility = View.GONE
                }
                try {
                    timer.cancel()
                } catch (e : Exception){

                }
                receive_ticket?.setOnClickListener {
                    showReceiveTicketDialog()
                }
                if (!isShowedDialog){
                    showReceiveTicketDialog()
                    isShowedDialog = true
                }
            }
            2 ->{//兑奖
                if (!StringUtils.isEmpty(statusbean.image)){
                    exchangePrize(statusbean.image)
                }
            }
        }
    }

    var isShowedDialog = false

    fun getWatchVideoStatus(){

        if (!UserHelper.isLogin(context)){
            //context?.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        if (getDayOfWeek() == 4){
            return
        }
        ApiManager.getInstance().getApiVideoTest().getVideoStatus(UserInfoViewModel.getUserId()).enqueue(object : Callback<DailyTaskStatusBean>{
            override fun onResponse(call: Call<DailyTaskStatusBean>, response: Response<DailyTaskStatusBean>) {
                if (response.body() != null){
                    val bean = response.body()!!
                    //if (bean?.data?.status == 2){//0是未看视频，1为未领取，2为已领取
                        /*val intent = Intent(context,DailyLotteryTicketActivity::class.java)
                        startActivity(intent)*/
                        //whenStatusZero()//当未看视频时调用 0时调用
                        //showReceiveTicketDialog()//当未领取时调用，1时调用
                    statusbean = bean.data
                    updateStatus()
                    //}

                    //loadVideo()
                }
            }

            override fun onFailure(call: Call<DailyTaskStatusBean>, t: Throwable) {

            }

        })

    }
    //为2时调用，判断Image是否为空，如果Image为空不显示弹框，否则显示弹框。
    fun exchangePrize(url : String){
        CustomDialog.show(activity as AppCompatActivity, R.layout.dialog_today_laiduijiang,object : CustomDialog.OnBindView{
            override fun onBind(dialog: CustomDialog?, v: View?) {
                if (v == null){
                    return
                }
                val close = v.findViewById<ImageView>(R.id.close)
                val lijiduijiang = v.findViewById<TextView>(R.id.lijiduijiang)
                val image = v.findViewById<ImageView>(R.id.image)
                close.setOnClickListener {
                    dialog?.doDismiss()
                }
                lijiduijiang.setOnClickListener {
                    dialog?.doDismiss()
                    val intent = Intent(context,LastLotteryActivity::class.java)
                    startActivity(intent)
                }
                Glide.with(context!!).load(url).into(image)

            }

        }).setCancelable(false)
    }

    fun updateVideoStatus(){
        ApiManager.getInstance().getApiVideoTest().updateVideoStatus(UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>>{

            override fun onResponse(call: Call<BaseHttpBean<Int>>, response: Response<BaseHttpBean<Int>>) {
                if (response.body() != null){
                    //val data = response.body()!!
                    getWatchVideoStatus()
                    //onVideoUpdated()
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

            }

        })
    }

    //计算视频的播放时间
    fun startCaculateVideoTime(){
        try {
            timer.schedule(timerTask,0,1000)
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        //返回时，推荐页面可见，则继续播放视频
        if (!isDataLoaded) {
            init()
            isDataLoaded = true
        } else {
            if (LotteryFragment.curPos == 1 && MainActivity.index == 1) {

                mVideoView?.start()
            }
        }

    }

    lateinit var time : TextView

    lateinit var controllerView: ToDayLotteryControllerView

    override fun playCurVideo(position: Int) {
        val count: Int = mVideoView!!.childCount
        for (i in 0 until count) {
            if (position == curPlayPos) {
                return
            }
            if (ivCover != null){
                ivCover?.visibility = View.VISIBLE
            }
            val itemView = viewPagerLayoutManager!!.findViewByPosition(position) ?: return
            val rootView = itemView.findViewById<ViewGroup>(R.id.rl_item_video_container)
            val likeView: LikeView = rootView.findViewById(R.id.lv_item_video_likeview)
            controllerView = rootView.findViewById(R.id.cv_item_video_controller)
            ivPlay = rootView.findViewById<ImageView>(R.id.ivplay)
            ivCover = rootView.findViewById<ImageView>(R.id.iv_item_video_cover)
            time = rootView.findViewById(R.id.time)
            ivPlay?.visibility = View.GONE
            ivPlay?.alpha = 0.4f

            image_activity_detail.setOnClickListener {
                if (!UserHelper.isLogin(context)){
                    context?.startActivity(Intent(context, LoginActivity::class.java))
                    return@setOnClickListener
                }
                if (TimeUtil.getDayOfWeek() == 4) {
                    CustomDialog.show(
                        requireActivity() as AppCompatActivity, R.layout.dialog_partner
                    ) { dialog, v -> //dialog.doDismiss();
                        var image = v.findViewById<ImageView>(R.id.bg)
                        val knowdetail = v.findViewById<ImageView>(R.id.knowdetail)
                        if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                            image.setImageDrawable(requireContext().getDrawable(R.mipmap.vipday_vip))
                            knowdetail.setOnClickListener {
                                dialog.doDismiss()
                                val intent = Intent(context, VipDayActivity::class.java)
                                intent.putExtra("activityId", LotteryFragment.activityId)
                                startActivity(intent)
                            }
                        } else {
                            image.setImageDrawable(requireActivity().getDrawable(R.mipmap.vipday_fan))
                            knowdetail.setOnClickListener {
                                dialog.doDismiss()
                                val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                                intent.putExtra("productId", 1)
                                startActivity(intent)
                            }
                        }
                    }
                    return@setOnClickListener
                }
                val intent = Intent(context,DailyLotteryTicketActivity::class.java)
                startActivity(intent)
            }
//播放暂停事件
            likeView?.setOnClickListener {
                if (mVideoView!!.isPlaying) {
                    mVideoView?.pause()
                    ivPlay?.visibility = View.VISIBLE
                } else {
                    mVideoView?.start()
                    ivPlay?.visibility = View.GONE
                }
            }
            /*//播放暂停事件
            likeView.setOnPlayPauseListener(object: LikeView.OnPlayPauseListener {
                override fun onPlayOrPause() {
                    if (mVideoView!!.isPlaying) {

                        mVideoView?.pause()
                        //timer.cancel()

                        ivPlay.visibility = View.VISIBLE
                    } else {

                        mVideoView?.start()
                        ivPlay.visibility = View.GONE
                    }
                }
            })*/
            curPlayPos = position
            detachParentView(rootView)//切换播放器位置

            autoPlayVideo(position, ivCover)

            likeShareEvent(controllerView) //评论点赞事件
        }
        updateStatus()
    }

    private fun autoPlayVideo(position: Int, ivCover: ImageView?) {
        playedVideo = dataBeanList?.get(position)   //当前正在播放的视频资源
        if (position == (dataBeanList!!.size - 1)){
            DialogUtil.toast(requireContext(),"已经是最后一个视频了")
        }
        mVideoView?.release()
        mVideoView?.setUrl(playedVideo!!.videoUrl)
        //加载视频
        if (!requireParentFragment().isHidden){
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
        publishId = playedVideo!!.userId.toString()

    }

    /**
     * 加载视频播放列表
     */
    private fun loadVideo(){
        val callback = object: Callback<LotteryVideoBean>{
            override fun onResponse(call: Call<LotteryVideoBean>, response: Response<LotteryVideoBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data == null){
                    return
                }
                dataBeanList = response.body()!!.data
                adapter =
                    ToDayLotteryVideoAdapter(
                        activity,
                        dataBeanList
                    )
                list?.adapter = adapter
                isDataLoaded = true
                getWatchVideoStatus()
            }

            override fun onFailure(call: Call<LotteryVideoBean>, t: Throwable) {
                t.message
            }
        }
        if (type == VideoType.TYPE_LASTLOTTERY){
            apiVideoTest.loadLashLotteryVideo(UserInfoViewModel.getUserId()).enqueue(callback)
        } else if (type == VideoType.TYPE_NOWDAYLOTTERY){
            apiVideoTest.loadNowDayLotteryVideo(UserInfoViewModel.getUserId()).enqueue(callback)
        } else if (type == VideoType.TYPE_WEEKLOTTERY){
            apiVideoTest.loadWeekLotteryVideo(UserInfoViewModel.getUserId()).enqueue(callback)
        } else if (type == VideoType.TYPE_VIPDAY){
            apiVideoTest.loadVIPDayLotteryVideo(UserInfoViewModel.getUserId()).enqueue(callback)
        }

    }

    /**
     * 用户操作事件
     */
    private fun likeShareEvent(controllerView: ToDayLotteryControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            /*点击【头像】*/
            override fun onHeadClick() {
                //RxBus.getDefault().post(MainPageChangeEvent(2))
            }

            /*点击【点赞】*/
            override fun onUpClick() {
                if (playedVideo?.isUp == 0) {
                    //ivUp.setImageResource(R.mipmap.btn_main_up_false)
                    val upCount = playedVideo?.upCount?:0 + 1
                    tvUpCount.text = upCount.toString()
                    ivUp.setImageResource(R.mipmap.btn_main_up_true)
                    playedVideo?.isUp = 1000
                    playedVideo?.upCount = upCount
                    apiVideoTest?.upVideo(UserInfoViewModel.getUserId(), videoId, publishId)
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<UpLikeBean>, response: Response<UpLikeBean>) {

                            }

                            override fun onFailure(call: Call<UpLikeBean>, t: Throwable) {
                                Log.i(TAG, "onFailure: ", t)
                            }
                        })
                } else {
                    //ivUp.setImageResource(R.mipmap.btn_main_up_true)
                }
            }

            /*点击【爱心】*/
            override fun onLikeClick() {
                apiVideoTest!!.likeVideo(UserInfoViewModel.getUserId(), videoId, publishId).enqueue(object: Callback<UpLikeBean>{
                    override fun onResponse(call: Call<UpLikeBean>, response: Response<UpLikeBean>) {
                        val likeState = response.body()!!.data.statusCode
                        var likeCount = playedVideo?.likeCount?:0
                        when(likeState){
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
                commentDialog.show(childFragmentManager, "")*/
                XPopup.Builder(requireContext())
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
                    .asCustom(CommentDialogV2(requireContext(),videoId,playedVideo?.userId.toString()))
                    .show()
            }

            /*点击【邀请码】*/
            override fun onShareClick() {
                ShareDialog().show(childFragmentManager, "")
            }
        })
    }
}