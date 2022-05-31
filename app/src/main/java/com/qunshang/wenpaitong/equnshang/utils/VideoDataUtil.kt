package com.qunshang.wenpaitong.equnshang.utils
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean
class VideoDataUtil {

    companion object {

        var list : List<RecommendVideoBean.DataBean>? = null

        fun setVideoData(list : List<RecommendVideoBean.DataBean>){
            VideoDataUtil.list = list
        }

        fun getVideoData() : List<RecommendVideoBean.DataBean>? {
            return list
        }

        fun clear(){
            VideoDataUtil.list = null
        }

    }

}