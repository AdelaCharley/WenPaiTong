package com.qunshang.wenpaitong.equnshang.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.DayAmount


class MyCommissionBodyAdapter(val context: Context, val data: List<DayAmount>, private val isLast: Boolean):
    RecyclerView.Adapter<MyCommissionBodyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_commission_body, parent, false)
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        holder.time.text = currentData.title
        holder.amount.text = currentData.amount
        if (isLast && position == data.size - 1) {  //最后一个item
            holder.root.background = context.getDrawable(R.drawable.bg_white_bottom_r6)
            holder.line.visibility = View.GONE
        } else {
            holder.root.setBackgroundColor(context.getColor(R.color.white))
            holder.line.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val amount: TextView = itemView.findViewById(R.id.tv_amount)
        val line: View = itemView.findViewById(R.id.cutoff_line)
    }
}