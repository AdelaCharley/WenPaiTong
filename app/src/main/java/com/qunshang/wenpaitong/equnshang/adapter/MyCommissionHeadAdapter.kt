package com.qunshang.wenpaitong.equnshang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.MyCommissionBean
import com.qunshang.wenpaitong.equnshang.view.dialog.PopupHintDialog


class MyCommissionHeadAdapter(val context: Context, val data: List<MyCommissionBean>?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_BODY -> {
                BodyViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_my_commission_head, parent, false))
            }
            else      -> {
                FootViewHolder(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_list_end_hint, parent, false))
            }
        }


    private var isLast = false
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BodyViewHolder) {
            val currentData = data?.get(position)
            holder.date.text = currentData?.title
            holder.profit.text = "预计收益 ￥ ${currentData?.amount}"
            holder.question.setOnClickListener { popupHint() }
            val layoutManager = LinearLayoutManager(context)
            holder.list.layoutManager = layoutManager
            isLast = position == (data?.size!! - 1)
            holder.list.adapter = MyCommissionBodyAdapter(context, currentData?.dayAmount!!, isLast)
        } else {
            holder as FootViewHolder
            holder.endHind.text = TEXT_END_HINT
        }
    }

    private fun popupHint() {
        val custom = PopupHintDialog(context, POPUP_HINT_TITLE, POPUP_HINT_CONTENT)
        XPopup.Builder(context)
            .moveUpToKeyboard(false)
            .enableDrag(true)
            .dismissOnTouchOutside(false)
            .asCustom(custom)
            .show()
    }

    override fun getItemCount(): Int = data!!.size + 1

    override fun getItemViewType(position: Int): Int =
        if (position < data!!.size) TYPE_BODY else TYPE_FOOT

    inner class BodyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val profit: TextView = itemView.findViewById(R.id.tv_profit)
        val question: ImageView = itemView.findViewById(R.id.img_question)
        val list: RecyclerView = itemView.findViewById(R.id.list)
    }

    inner class FootViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val endHind: TextView = itemView.findViewById(R.id.tv_end_hint)
    }

    companion object {
        private const val TYPE_BODY = 1
        private const val TYPE_FOOT = 2
        private const val TEXT_END_HINT = ""
        private const val POPUP_HINT_TITLE = "关于收益的重要说明"
        private const val POPUP_HINT_CONTENT =
            "　　佣金收益均来自于下属用户购买版通积分时按比例获得；当日所产生的佣金收益将于7天后进行计算；实际到账金额因税筹等原因会与预计收益金额有所浮动。"
        }
}