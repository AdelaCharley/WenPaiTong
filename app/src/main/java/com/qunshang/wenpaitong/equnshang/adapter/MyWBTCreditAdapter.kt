package com.qunshang.wenpaitong.equnshang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.WBTCreditBean

/**
 * create by 何姝霖
 */
class MyWBTCreditAdapter(private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val creditLog: MutableList<WBTCreditBean> = mutableListOf()
    private var creditAmount = "0"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_HEAD -> {
                HeadViewHolder(LayoutInflater.from(parent.context)
                                   .inflate(R.layout.item_my_wbt_credit_head, parent, false))
            }
            TYPE_BODY -> {
                BodyViewHolder(LayoutInflater.from(parent.context)
                                   .inflate(R.layout.item_my_wbt_credit_body, parent, false))
            }
            else      -> {
                FootViewHolder(LayoutInflater.from(parent.context)
                                   .inflate(R.layout.item_list_end_hint, parent, false))
            }
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeadViewHolder) {
            holder.point.text = creditAmount
        } else if (holder is BodyViewHolder) {
            val currentData = creditLog[position - 1]
            holder.title.text = currentData.title
            holder.time.text = currentData.createTime
            holder.credit.text = currentData.amount
            if (position == creditLog.size) {
                holder.root.background = context.getDrawable(R.drawable.bg_white_bottom_r6)   //底部圆角
                holder.line.visibility = View.INVISIBLE //分割线
            } else {
                holder.root.background = context.getDrawable(R.color.white)
                holder.line.visibility = View.VISIBLE
            }
        } else {
            holder as FootViewHolder
            holder.endHind.text = TEXT_END_HINT
        }
    }

    override fun getItemCount(): Int = 1 + creditLog.size + 1

    override fun getItemViewType(position: Int): Int =
        if (position == 0) TYPE_HEAD
        else if (position == itemCount - 1) TYPE_FOOT
        else TYPE_BODY

    @SuppressLint("NotifyDataSetChanged")
    fun add(creditDetail: List<WBTCreditBean>) {
        creditLog.addAll(creditDetail)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCreditAmount(amount: String) {
        creditAmount = amount
        notifyDataSetChanged()
    }

    inner class HeadViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val point: TextView = itemView.findViewById(R.id.tv_point)
    }

    inner class BodyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val root: ConstraintLayout = itemView.findViewById(R.id.layout_root)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val credit: TextView = itemView.findViewById(R.id.tv_credit)
        val line: View = itemView.findViewById(R.id.view_line)
    }

    inner class FootViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val endHind: TextView = itemView.findViewById(R.id.tv_end_hint)
    }

    companion object {
        private const val TYPE_HEAD = 0
        private const val TYPE_BODY = 1
        private const val TYPE_FOOT = 2
        private const val TEXT_END_HINT = "仅展示近十二个月明细，需查看更多明细请联系平台客服"
    }
}