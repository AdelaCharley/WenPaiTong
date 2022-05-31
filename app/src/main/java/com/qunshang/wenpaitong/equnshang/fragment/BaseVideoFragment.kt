package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.ButterKnife
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.interfaces.OnViewPagerListener
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.view.ViewPagerLayoutManager
import xyz.doikki.videoplayer.player.VideoView

abstract class BaseVideoFragment : MyBaseFragment() {

    abstract fun setLayoutId(): Int

    abstract fun init()

    var isDataLoaded = false

    companion object{
        var commentTitle: String = ""   //当前视频评论标题（“n条评论”）
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(setLayoutId(), container, false)
        ButterKnife.bind(this, rootView)
        list = rootView.findViewById(R.id.list)
        refreshLayout = rootView.findViewById(R.id.refreshLayout)
        refreshLayout.isEnabled = false
        return rootView
    }

    fun getIsDataLoaded() : Boolean {
        return isDataLoaded
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

    var mVideoView: VideoView<*>? = null

    var viewPagerLayoutManager: ViewPagerLayoutManager? = null

    var curPlayPos = -1 //当前播放视频的位置

    var ivCurCover: ImageView? = null

    var videoId = 0                 //当前视频id

    lateinit var refreshLayout : SwipeRefreshLayout

    override fun onPause() {
        super.onPause()
        mVideoView?.pause()
    }
    override fun onStop() {
        super.onStop()
        mVideoView?.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        mVideoView?.release()
    }

    fun pause(){
        mVideoView?.pause()
    }

    fun start(){
        mVideoView?.start()
        if (ivPlay != null){
            ivPlay?.visibility = View.GONE
        }
    }

    var ivPlay : ImageView ? = null

    var ivCover : ImageView ? = null// rootView.findViewById<ImageView>(R.id.iv_item_video_cover)

    var list : RecyclerView ? = null

    fun setViewPagerLayoutManager() {
        StringUtils.log("awononglea")
        viewPagerLayoutManager = ViewPagerLayoutManager(activity)
        StringUtils.log("awononglea1")
        list?.layoutManager = viewPagerLayoutManager
        if (list == null){
            StringUtils.log("list wei kong ")
        }
        StringUtils.log("awononglea2")
        //list.layoutManager == null
        list?.scrollToPosition(0)
        StringUtils.log("awononglea3")
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

    abstract fun playCurVideo(position: Int)

    fun detachParentView(rootView: ViewGroup) {

        if ((rootView == null) or (mVideoView == null)){
            return
        }

        //1.添加mVideoview到当前需要播放的item中,添加进item之前，保证ijkmVideoView没有父view
        mVideoView?.parent?.let {
            (it as ViewGroup).removeView(mVideoView)

        }
        rootView.addView(mVideoView, 0)
    }

    var isEnd = false //是否在播放尾部视频

}