package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View

import kotlinx.android.synthetic.main.activity_watch_video.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.VendorVideoBean
import xyz.doikki.videoplayer.player.VideoView

class WatchVideoActivity : BaseActivity (){

    var bean : VendorVideoBean.DataBean ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_video)
        bean = intent.getSerializableExtra("data") as VendorVideoBean.DataBean?
        if (bean == null){
            return
        }
        toolbar_back.setOnClickListener { finish() }
        videoview?.addOnStateChangeListener(object : VideoView.OnStateChangeListener{
            override fun onPlayerStateChanged(playerState: Int) {

            }

            override fun onPlayStateChanged(playState: Int) {
                if ((playState == VideoView.STATE_PAUSED)){
                    ivplay.visibility = View.VISIBLE
                } else if (playState == VideoView.STATE_PLAYING){
                    ivplay.visibility = View.GONE
                }
            }

        })
        titlestr.setText(bean!!.title)
        time.setText(bean!!.createTime)
        videoview.setUrl(bean!!.videoUrl)
        videoview.setOnClickListener {
            if (videoview.isPlaying){
                videoview.pause()
            } else {
                videoview.start()
            }
        }
        videoview.setLooping(true)
        videoview.start()
    }

    override fun onPause() {
        super.onPause()
        videoview.pause()
    }

    override fun onResume() {
        super.onResume()
        videoview.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoview.release()
    }

}