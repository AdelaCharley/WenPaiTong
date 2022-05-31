package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.dialog_wenbantong_apply_reason.view.*
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongApplyExchangeActivity

class WenBanTongApplyReasonDialog(context: Context) : BottomPopupView(context) {

    var index = -1

    lateinit var texts : Array<TextView>
    lateinit var imgs : Array<ImageView>

    lateinit var layouts : Array<View>

    override fun onCreate() {
        super.onCreate()
        texts = arrayOf(text1,text2)
        imgs = arrayOf(img1,img2)
        layouts = arrayOf(layout1,layout2)
        for (i in layouts.indices){
            layouts[i].setOnClickListener(MyListener(i))
        }
        confirm.setOnClickListener {
            if (index == -1){
                return@setOnClickListener
            }
            val activity = context as WenBanTongApplyExchangeActivity
            activity.setReason(texts[index].text.toString())
            dismiss()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_wenbantong_apply_reason
    }

    inner class MyListener(val index : Int) : OnClickListener{
        override fun onClick(v: View?) {
            if (this@WenBanTongApplyReasonDialog.index != index){
                if (this@WenBanTongApplyReasonDialog.index != -1){
                    imgs[this@WenBanTongApplyReasonDialog.index].setImageDrawable(context.getDrawable(R.mipmap.wenbantong_apply_exchange_apply_exchange))
                    texts[this@WenBanTongApplyReasonDialog.index].setTextColor(Color.parseColor("#ff66645f"))
                }
                imgs[index].setImageDrawable(context.getDrawable(R.mipmap.wenbantong_apply_exchange_money_selected))
                texts[index].setTextColor(Color.parseColor("#ff312e2a"))
                this@WenBanTongApplyReasonDialog.index = index
                confirm.background = context.getDrawable(R.drawable.background_wenbantong_gobuynow)
            }
        }

    }

}