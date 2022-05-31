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
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiVideo
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.interfaces.OnVideoControllerListener
import com.qunshang.wenpaitong.equnshang.view.*
import kotlinx.android.synthetic.main.fragment_qi_shi_tong_video.*
import kotlinx.android.synthetic.main.layout_controllerview_uplikecomment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.adapter.QiShiTongVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.QiShiTongVideoBean
import com.qunshang.wenpaitong.equnshang.data.UpLikeBean
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil
import xyz.doikki.videoplayer.player.AbstractPlayer
import xyz.doikki.videoplayer.player.VideoView

class QiShiTongVideoFragment : BaseVideoFragment() {
    private var adapter: QiShiTongVideoAdapter? = null

    private var dataBeanList: MutableList<QiShiTongVideoBean.DataBean>? = null   //视频播放列表
    private var playedVideo: QiShiTongVideoBean.DataBean? = null                 //当前正在播放的视频
    //private val userId: String = UserInfoViewModel.getUserId()    //登录用户的id
    private var publishId: String = ""      //当前视频发布者id

    private val apiVideoTest: ApiVideo = ApiManager.getInstance().getApiVideoTest()

    override fun setLayoutId(): Int {
        return R.layout.fragment_qi_shi_tong_video
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
    fun update(bean: ManufactureChangeBean) {
        if (dataBeanList == null){
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

    override fun init(){
        mVideoView = VideoView<AbstractPlayer>(requireActivity().applicationContext)
        mVideoView = player
        mVideoView?.setLooping(true)
        loadVideo()
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

    override fun onResume() {
        super.onResume()
        //返回时，推荐页面可见，则继续播放视频
        if (!isDataLoaded){
            init()
        } else {
            if (MainFragment.curPage == 0 && MainActivity.index == 0) {
                mVideoView?.start()
            }
        }
    }

    /**
     * 循环播放视频列表
     */

    lateinit var controllerView: QiShiTongControllerView

    override fun playCurVideo(position: Int) {
        val count: Int = mVideoView!!.childCount
        for (i in 0 until count) {
            if (position == curPlayPos) {
                return
            }
            val itemView = viewPagerLayoutManager!!.findViewByPosition(position) ?: return
            val rootView = itemView.findViewById<ViewGroup>(R.id.rl_item_video_container)
            val likeView: LikeView = rootView.findViewById(R.id.lv_item_video_likeview)
            controllerView = rootView.findViewById(R.id.cv_item_video_controller)
            ivPlay = rootView.findViewById(R.id.ivplay)
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
        mVideoView!!.release()
        mVideoView!!.setUrl(playedVideo!!.videoUrl)      //加载视频
        if (parentFragment != null){
            if (!requireParentFragment().isHidden){
                mVideoView!!.start()
            }
        } else {
            mVideoView!!.start()
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
        publishId = playedVideo!!.manufacturerId.toString()

    }

    /**
     * 加载视频播放列表
     */

    private fun loadVideo(){
        val callback = object: Callback<QiShiTongVideoBean>{
            override fun onResponse(call: Call<QiShiTongVideoBean>, response: retrofit2.Response<QiShiTongVideoBean>) {
                if (response.body() == null){
                    return
                }
                dataBeanList = response.body()!!.data
                //Log.i(TAG,"" + dataBeanList?.size.toString())
                adapter =
                    QiShiTongVideoAdapter(activity, dataBeanList)
                list?.adapter = adapter
                isDataLoaded = true
            }

            override fun onFailure(call: Call<QiShiTongVideoBean>, t: Throwable) {
                //Log.i(TAG,"" + t.message)
                t.message
            }
        }
        apiVideoTest.loadQiShiTongVideo(UserInfoViewModel.getUserId()).enqueue(callback)
    }

    /**
     * 用户操作事件
     */

    private fun likeShareEvent(controllerView: QiShiTongControllerView) {
        controllerView.setListener(object : OnVideoControllerListener {
            /*点击【头像】*/
            override fun onHeadClick() {

            }

            /*点击【点赞】*/
            override fun onUpClick() {
                if (playedVideo!!.isUp == 0) {
                    apiVideoTest.upManufactureVideo(UserInfoViewModel.getUserId(), videoId.toString())
                        .enqueue(object : Callback<UpLikeBean> {
                            override fun onResponse(call: Call<UpLikeBean>, response: retrofit2.Response<UpLikeBean>) {
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
                apiVideoTest.likeManufactureVideo(UserInfoViewModel.getUserId(), videoId.toString()).enqueue(object: Callback<UpLikeBean>{
                    override fun onResponse(call: Call<UpLikeBean>, response: retrofit2.Response<UpLikeBean>) {
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
                /*val commentDialog = ManufactureCommentDialog(videoId, commentTitle)
                commentDialog.show(childFragmentManager, "")*/
                XPopup.Builder(requireContext())
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
                    .asCustom(ManufactureCommentDialogV2(requireContext(),videoId,publishId))
                    .show()
            }

            /*点击【邀请码】*/
            override fun onShareClick() {
                ShareDialog().show(childFragmentManager, "")
            }
        })
    }
}