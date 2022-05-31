package com.qunshang.wenpaitong.equnshang.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.qunshang.wenpaitong.R

/**
 *
 * create by 何姝霖
 */
class AfterSaleProgressAdapter(private val orderAfterSaleType: Int, private val orderAfterSaleStatus: Int) : RecyclerView.Adapter<AfterSaleProgressAdapter.ViewHolder>() {

    private val textContent = when(orderAfterSaleType) {
        10 -> arrayOf("提交申请","商家审核","退款","完成")
        20 -> arrayOf("提交申请","商家审核","买家发货","商家收货","退款中","完成")
        else -> arrayOf("提交申请","商家审核","买家发货","商家收货","换新","完成")
    }

    private val stepTotalNum = if (orderAfterSaleStatus == 0){2}
    else if (orderAfterSaleStatus == 1){ textContent.size }
    else if (orderAfterSaleStatus == 250 || orderAfterSaleStatus == 350){5}
    else{ orderAfterSaleStatus / 10 % 10 + 1 }

    private val isFail = orderAfterSaleStatus == 250 || orderAfterSaleStatus == 350 || orderAfterSaleStatus == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.item_after_sale_progress, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) { holder.lineLeft.visibility = View.INVISIBLE }
        if (position == stepTotalNum-1) {
            holder.lineRight.visibility = View.INVISIBLE
            if (isFail) {
                holder.node.load(R.mipmap.ic_after_sale_progress_fail)
                holder.content.text = "审核失败"
                holder.content.setTextColor(Color.parseColor("#F3594F"))
            } else {
                holder.node.load(R.mipmap.ic_after_sale_progress_finished)
                holder.content.text = textContent[position]
                holder.content.setTextColor(Color.parseColor("#32C126"))
            }
            return
        }
        holder.node.load(R.drawable.circle_32c126_r8)
        holder.content.text = textContent[position]
    }

    override fun getItemCount(): Int = stepTotalNum

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lineLeft: View = itemView.findViewById(R.id.line_left)
        val lineRight: View = itemView.findViewById(R.id.line_right)
        val node: ImageView = itemView.findViewById(R.id.node_default)
        val content: TextView = itemView.findViewById(R.id.content)
    }
}