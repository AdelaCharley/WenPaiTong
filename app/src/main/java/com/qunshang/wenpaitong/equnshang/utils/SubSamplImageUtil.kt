package com.qunshang.wenpaitong.equnshang.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.shizhefei.view.largeimage.LargeImageView
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory
import java.io.File

class SubSamplImageUtil {

    companion object{
        @SuppressLint("CheckResult")
        fun loadLargeImage(context: Context, res: String, imageView: SubsamplingScaleImageView) {
            imageView.isQuickScaleEnabled = false
            imageView.maxScale = 15F;
            imageView.isZoomEnabled = false;
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)

            Glide.with(context).load(res).downloadOnly(object : SimpleTarget<File?>() {
                override fun onResourceReady(resource: File, glideAnimation: Transition<in File?>?) {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(resource.absolutePath, options)
                    val sWidth = options.outWidth
                    val sHeight = options.outHeight
                    options.inJustDecodeBounds = false
                    val wm = ContextCompat.getSystemService(context, WindowManager::class.java)
                    val width = wm?.defaultDisplay?.width ?: 0
                    val height = wm?.defaultDisplay?.height ?: 0
                    if (sHeight >= height
                        && sHeight / sWidth >= 3) {
                        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                        imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), ImageViewState(0.5f, PointF(0f, 0f), 0))
                        StringUtils.log("好了，我执行了这个，" + resource.absolutePath + "我下载的地址是" + res)
                    } else {
                        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM)
                        imageView.setImage(ImageSource.uri(Uri.fromFile(resource)))
                        imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE)
                        StringUtils.log("好了，我执行了那个，" + resource.absolutePath + "我下载的地址是" + res)
                    }
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                }
            })
        }
        @SuppressLint("CheckResult")
        fun loadLargeImageWithLargeImageView(context: Context, res: String, imageView: LargeImageView) {

            Glide.with(context).load(res).downloadOnly(object : SimpleTarget<File?>() {
                override fun onResourceReady(resource: File, glideAnimation: Transition<in File?>?) {
                    imageView.setImage(FileBitmapDecoderFactory(resource))
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                }
            })
        }
    }
}