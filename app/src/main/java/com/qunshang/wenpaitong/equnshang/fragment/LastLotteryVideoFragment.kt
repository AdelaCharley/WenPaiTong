package com.qunshang.wenpaitong.equnshang.fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.MainActivity
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import kotlinx.android.synthetic.main.fragment_last_lottery_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.doikki.videoplayer.player.AbstractPlayer
import xyz.doikki.videoplayer.player.VideoView
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.LastLotteryVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.*
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import com.qunshang.wenpaitong.equnshang.view.*

/**
 * ”首页-推荐“页，仿抖音播放视频
 * create by libo
 * create on 2020-05-19
 * modifier 何姝霖
 * last-modified 2021-08-09
 */
class LastLotteryVideoFragment(var type : Int) : BaseVideoFragment() {

    private var adapter: LastLotteryVideoAdapter? = null

    private var dataBeanList: MutableList<LotteryVideoBean.DataBean>? = null   //视频播放列表

    private var playedVideo: LotteryVideoBean.DataBean? = null                 //当前正在播放的视频

    private var publishId: String = ""      //当前视频发布者id

    private val apiVideoTest: ApiVideo = ApiManager.getInstance().getApiVideoTest()

    override fun setLayoutId(): Int {
        return R.layout.fragment_last_lottery_video
    }

    companion object {
        var lotterier = ""
    }


    override fun init() {
        mVideoView = VideoView<AbstractPlayer>(requireActivity().applicationContext)
        mVideoView = player
        mVideoView?.setLooping(true)
        load()
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
    }

    fun load(){
        ApiManager.getInstance().getApiVideoTest().loadLastUserGetLottery().enqueue(object : Callback<BaseHttpBean<String>>{
            override fun onResponse(
                call: Call<BaseHttpBean<String>>,
                response: Response<BaseHttpBean<String>>
            ) {
                if (response.body() != null){
                    lotterier = response.body()?.data!!
                    loadVideo()
                }
            }

            override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        //返回时，推荐页面可见，则继续播放视频
        Log.i("zhangjuniii",this.javaClass.name + "的onResume被调用")
        if (!isDataLoaded){
            init()
            isDataLoaded = true
        } else {
            if (LotteryFragment.curPos == 0  && MainActivity.index == 1) {
                mVideoView?.start()
            }
        }
    }

    lateinit var controllerView : LastLotteryControllerView

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
            ivPlay?.visibility = View.GONE
            ivPlay?.alpha = 0.4f
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
                    LastLotteryVideoAdapter(
                        activity,
                        dataBeanList
                    )
                list?.adapter = adapter
                isDataLoaded = true
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

    /**
     * 用户操作事件
     */
    private fun likeShareEvent(controllerView: LastLotteryControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            /*点击【头像】*/
            override fun onHeadClick() {
                //RxBus.getDefault().post(MainPageChangeEvent(2))
            }

            /*点击【点赞】*/
            override fun onUpClick() {
                //Toast.makeText(context,"点击点赞按钮",Toast.LENGTH_SHORT).show()
                if (playedVideo!!.isUp == 0) {
                    //Toast.makeText(context,"开始点赞",Toast.LENGTH_SHORT).show()
                    //ivUp.setImageResource(R.mipmap.btn_main_up_false)
                    val upCount = playedVideo!!.upCount + 1
                    tvUpCount.text = upCount.toString()
                    ivUp.setImageResource(R.mipmap.btn_main_up_true)
                    playedVideo!!.isUp = 1000
                    playedVideo!!.upCount = upCount
                    apiVideoTest.upVideo(UserInfoViewModel.getUserId(), videoId, publishId)
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<
                                    UpLikeBean>, response: Response<UpLikeBean>) {

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
                apiVideoTest!!.likeVideo(UserInfoViewModel.getUserId(), videoId, publishId).enqueue(object: Callback<UpLikeBean>{
                    override fun onResponse(call: Call<UpLikeBean>, response: Response<UpLikeBean>) {
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

            override fun onShareClick() {
                ShareDialog().show(childFragmentManager, "")
            }
        })
    }
}