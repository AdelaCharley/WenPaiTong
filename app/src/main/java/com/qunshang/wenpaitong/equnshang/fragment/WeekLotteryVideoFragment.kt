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
import com.kongzue.dialog.v3.CustomDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import kotlinx.android.synthetic.main.fragment_week_lottery_video.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import kotlinx.android.synthetic.main.view_controller_week_lottery.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.adapter.WeekLotteryVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper
import com.qunshang.wenpaitong.equnshang.view.*
import xyz.doikki.videoplayer.player.AbstractPlayer
import xyz.doikki.videoplayer.player.VideoView
import java.lang.Exception
import java.util.*

/**
 * ”首页-推荐“页，仿抖音播放视频
 * create by libo
 * create on 2020-05-19
 * modifier 何姝霖
 * last-modified 2021-08-09
 */
//更改，这个是往期抽奖
class WeekLotteryVideoFragment(var type : Int) : BaseVideoFragment() {
    private var adapter: WeekLotteryVideoAdapter? = null

    private var dataBeanList: MutableList<LotteryVideoBean.DataBean>? = null   //视频播放列表
    private var playedVideo: LotteryVideoBean.DataBean? = null                 //当前正在播放的视频
    private var publishId: String = ""      //当前视频发布者id

    private val apiVideoTest: ApiVideo = ApiManager.getInstance().getApiVideoTest()

    var isVideoWatched = false

    var isReceived = true

    override fun setLayoutId(): Int {
        return R.layout.fragment_week_lottery_video
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView?.release()
        timer.cancel()
        EventBus.getDefault().unregister(this)
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
            if (adapter!!.datas!!.get(i).getUserId() == bean.id) {
                if (bean.isFollow) {
                    adapter!!.datas!!.get(i).setIsFocus(0)
                } else {
                    adapter!!.datas!!.get(i).setIsFocus(222)
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

    override fun init() {
        mVideoView = VideoView<AbstractPlayer>(requireActivity().applicationContext)
        mVideoView = player
        mVideoView?.setLooping(true)

        setViewPagerLayoutManager()
        setRefreshEvent()
        mVideoView?.addOnStateChangeListener(object : VideoView.OnStateChangeListener{
            override fun onPlayerStateChanged(playerState: Int) {
                Log.i(Constants.logtag,"正在播放")
                if (parentFragment == null){
                    return
                }
            }

            override fun onPlayStateChanged(playState: Int) {
                Log.i("playerstate","当前播放器的状态是" + playState)
                if (playState == VideoView.STATE_PLAYING){
                    TimeUtil.startComputeTimer()
                }
                if (playState == VideoView.STATE_PAUSED){
                    TimeUtil.pauseCompute()
                }

                if ((playState == VideoView.STATE_BUFFERED) or (playState == VideoView.STATE_PLAYBACK_COMPLETED)){
                    if (!isEnd){

                    }
                    if (isVideoWatched){//视频看完了，不需要更新状态。
                        return
                    }

                }
            }

        })
        loadVideo()
        //getVideoStatus()
    }

    //因为本设计中的RecyclerView需要刷新，当切换视频页的时候有可能会不刷新，所以在此使用一个方法刷新状态
    fun updateStatus(){
        if (!this::statusbean.isInitialized){
            return
        }
        if (statusbean.status == 3){ //未看视频
            if (this@WeekLotteryVideoFragment::text.isInitialized){
                text.visibility = View.VISIBLE
            }
        } else {
            isVideoWatched = true
            if (this@WeekLotteryVideoFragment::text.isInitialized){
                text.visibility = View.GONE
            }

        }
        if (statusbean.status == 2){//未领取任务
            isReceived = false
            if (this@WeekLotteryVideoFragment::receive.isInitialized){
                receive.visibility = View.VISIBLE
                receive.setOnClickListener {
                    showReceiveTicketDialog()
                }
            }
            if (!isShowedDialog){
                showReceiveTicketDialog()
                isShowedDialog = true
            }
        }
        //DialogUtil.toast(context,"我获取了" + statusbean.status.toString())
        if (statusbean.status == 0){//已领取任务未完成
            //DialogUtil.toast(requireContext(),"这一个状态是0")
            whenStatusZero()
        }
        if (statusbean.status == 1){//已完成任务
            receive.visibility = View.GONE
            if (this::receive_prize.isInitialized){
                receive_prize.visibility = View.VISIBLE
                receive_prize.setOnClickListener { showReceiveDialog() }
            }
            if (this::text_receive_prize.isInitialized){
                text_receive_prize.visibility = View.VISIBLE
            }
            showReceiveDialog()
        }
    }

    fun showReceiveDialog(){
        CustomDialog.show(activity as AppCompatActivity, R.layout.dialog_today_laiduijiang,object : CustomDialog.OnBindView{
            override fun onBind(dialog: CustomDialog?, v: View?) {
                if (v == null){
                    return
                }
                val close = v.findViewById<ImageView>(R.id.close)
                val lijiduijiang = v.findViewById<TextView>(R.id.lijiduijiang)
                val image = v.findViewById<ImageView>(R.id.image)
                var bg = v.findViewById<ImageView>(R.id.bg)
                bg.setImageDrawable(requireContext().getDrawable(R.mipmap.dialog_week_laiduijiang))
                close.setOnClickListener {
                    dialog?.doDismiss()
                }
                lijiduijiang.setOnClickListener {
                    dialog?.doDismiss()
                    val intent = Intent(context, WeekPrizeHistoryActivity::class.java)
                    intent.putExtra("taskUserRelationId", statusbean.taskUserRelationId.toString())
                    context!!.startActivity(intent)
                }
                Glide.with(context!!).load(statusbean.prizeImageRow).into(image)

            }

        }).setCancelable(false)
    }

    var isShowedDialog = false//让弹窗只显示一次

    lateinit var statusbean : WeekVideoStatus.DataBean

    //获取用户每周任务的完成状况
    fun getVideoStatus(){
        if (!UserHelper.isLogin(context)){
            return
        }
        ApiManager.getInstance().getApiLotteryTest().getWeekStatus(UserInfoViewModel.getUserId()).enqueue(object : Callback<WeekVideoStatus>{
            override fun onResponse(call: Call<WeekVideoStatus>, response: Response<WeekVideoStatus>) {
                if (response.body() == null){
                    DialogUtil.toast(context,"出错了")
                }
                val bean = response.body()?.data!!
                statusbean = bean
                updateStatus()
            }

            override fun onFailure(call: Call<WeekVideoStatus>, t: Throwable) {

            }

        })
    }

    lateinit var invitelistdatabean : WeekInviteListDataBean.DataBean

    //当看完视频且已领取任务的时候调用
    fun whenStatusZero(){
        receive.visibility = View.GONE
        currentprogress.visibility = View.VISIBLE
        group_progress.visibility = View.VISIBLE
        remaintime.visibility = View.VISIBLE
        if (!this::invitelistdatabean.isInitialized){
            ApiManager.getInstance().getApiVideoTest().loadWeekInviteList(UserInfoViewModel.getUserId()).enqueue(object : Callback<WeekInviteListDataBean>{
                override fun onResponse(call: Call<WeekInviteListDataBean>, response: Response<WeekInviteListDataBean>) {
                    if (response.body() == null){
                        return
                    }
                    this@WeekLotteryVideoFragment.invitelistdatabean = response.body()!!.data
                    updateZeroStatus()
                }

                override fun onFailure(call: Call<WeekInviteListDataBean>, t: Throwable) {

                }
            })
        } else {
            updateZeroStatus()
        }
    }

    fun updateZeroStatus(){
        val count = invitelistdatabean.invitingList.size
        var totalcount = 10
        if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
            totalcount = 5
        }
        currentprogress.setText("当前进度:" + count + "/" + totalcount)
        group_progress.progress = count
        group_progress.max = totalcount
        try {
            timer.schedule(timertask, 1000, 1000)
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    val timertask: TimerTask = object : TimerTask() {
        override fun run() {
            val activity = requireActivity()
            activity.runOnUiThread {
                remaintime?.setText(TimeUtil.getTimeRemainByDayString(statusbean.taskEndTime))
            }
        }
    }

    val timer = Timer()

    fun showReceiveTicketDialog() {
        CustomDialog.show(requireActivity() as AppCompatActivity,R.layout.dialog_week_receive_ticket,object : CustomDialog.OnBindView{
            override fun onBind(dialog: CustomDialog?, v: View?) {
                val close = v!!.findViewById<ImageView>(R.id.close)
                val desc = v.findViewById<TextView>(R.id.desc)
                if (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2){
                    desc.setText("邀请5人为你助力，好礼直接拿")
                }
                close.setOnClickListener {
                    dialog?.doDismiss()
                }
                val receiveticket = v.findViewById<TextView>(R.id.receivetask)
                receiveticket.setOnClickListener {
                    ApiManager.getInstance().getApiLotteryTest()
                        .receiveTicket(UserInfoViewModel.getUserId())
                        .enqueue(object : Callback<WeekReceiveTicketBean> {

                            override fun onResponse(
                                call: Call<WeekReceiveTicketBean>,
                                response: Response<WeekReceiveTicketBean>
                            ) {
                                if (response.body() != null) {
                                    dialog?.doDismiss()
                                    val data = response.body()
                                    if (data?.code == 200) {
                                        DialogUtil.toast(context, "领取成功")
                                        //whenStatusZero()
                                        getVideoStatus()
                                    } else {
                                        DialogUtil.toast(context, data?.msg)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<WeekReceiveTicketBean>, t: Throwable) {

                            }

                        })
                }
            }

        }).setCancelable(false)
    }

    override fun onResume() {
        super.onResume()
        //返回时，推荐页面可见，则继续播放视频
        if (!isDataLoaded){
            init()
            isDataLoaded = true
        } else {
            if (LotteryFragment.curPos == 2  && MainActivity.index == 1) {
            mVideoView?.start()
        }
        }
    }

    lateinit var text : TextView

    lateinit var receive : ImageView

    lateinit var detail : ImageView

    lateinit var receive_prize : ImageView

    lateinit var text_receive_prize : TextView

    lateinit var controllerView: WeekLotteryControllerView

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
            ivPlay = rootView.findViewById(R.id.ivplay)
            ivCover = rootView.findViewById<ImageView>(R.id.iv_item_video_cover)
            detail = rootView.findViewById(R.id.detail)
            detail.setOnClickListener {
                if (!UserHelper.isLogin(requireContext())){
                    startActivity(Intent(context, LoginActivity::class.java))
                    return@setOnClickListener
                }
                val intent = Intent(context,WeekActivitysActivity::class.java)
                startActivity(intent)
            }
            receive = rootView.findViewById(R.id.receive)
            receive.setOnClickListener {
                showReceiveTicketDialog()
            }

            if (UserHelper.isLogin(requireContext())){
                if (!isReceived){
                    receive.visibility = View.VISIBLE
                    receive.setOnClickListener {
                        showReceiveTicketDialog()
                    }
                }
            }

            receive_prize = rootView.findViewById(R.id.receive_prize)
            text_receive_prize = rootView.findViewById(R.id.text_receive_prize)

            if (this::statusbean.isInitialized){
                if (statusbean.status == 1){
                    receive_prize.visibility = View.VISIBLE
                    text_receive_prize.visibility = View.VISIBLE
                    receive_prize.setOnClickListener {
                        showReceiveDialog()
                    }
                }
            }

            ivPlay?.visibility = View.GONE
            ivPlay?.alpha = 0.4f

            text = rootView.findViewById(R.id.watchtoget)
            if (isVideoWatched){
                text.visibility = View.GONE
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
            //播放暂停事件
            /*likeView.setOnPlayPauseListener(object: LikeView.OnPlayPauseListener {
                override fun onPlayOrPause() {
                    if (mVideoView!!.isPlaying) {
                        mVideoView?.pause()
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
        mVideoView?.release()
        mVideoView?.setUrl(playedVideo!!.videoUrl)      //加载视频
        if (position == (dataBeanList!!.size - 1)){
            DialogUtil.toast(requireContext(),"已经是最后一个视频了")
        }
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
                dataBeanList = response.body()!!.data
                adapter =
                    WeekLotteryVideoAdapter(
                        activity,
                        dataBeanList
                    )
                list?.adapter = adapter
                isDataLoaded = true
                if (UserHelper.isLogin(context)){
                    getVideoStatus()
                    return
                }

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

    private fun likeShareEvent(controllerView: WeekLotteryControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            override fun onHeadClick() {
            }

            /*点击【点赞】*/
            override fun onUpClick() {
                if (playedVideo!!.isUp == 0) {
                    //ivUp.setImageResource(R.mipmap.btn_main_up_false)
                    val upCount = playedVideo!!.upCount + 1
                    tvUpCount.text = upCount.toString()
                    ivUp.setImageResource(R.mipmap.btn_main_up_true)
                    playedVideo!!.isUp = 1000
                    playedVideo!!.upCount = upCount
                    apiVideoTest.upVideo(UserInfoViewModel.getUserId(), videoId, publishId)
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<UpLikeBean>, response: Response<UpLikeBean>) {
                                if (response.body() == null){
                                    return
                                }

                            }

                            override fun onFailure(call: Call<UpLikeBean>, t: Throwable) {
                            }
                        })
                } else {
                    //ivUp.setImageResource(R.mipmap.btn_main_up_true)
                }
            }

            /*点击【爱心】*/
            override fun onLikeClick() {
                apiVideoTest.likeVideo(UserInfoViewModel.getUserId(), videoId, publishId).enqueue(object: Callback<UpLikeBean>{
                    override fun onResponse(call: Call<UpLikeBean>, response: Response<UpLikeBean>) {
                        if (response.body() == null){
                            return
                        }
                        val likeState = response.body()!!.data.statusCode
                        var likeCount = playedVideo!!.likeCount
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
                val dialog = CommentDialogV2(requireContext(),videoId,playedVideo?.userId.toString())
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
                    .asCustom(dialog)
                    .show()
            }

            /*点击【邀请码】*/
            override fun onShareClick() {
                ShareDialog().show(childFragmentManager, "")
            }
        })
    }
}