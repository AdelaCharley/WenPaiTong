package com.qunshang.wenpaitong.equnshang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.SystemInformAdapter.SystemInformViewHolder
import com.qunshang.wenpaitong.equnshang.data.SystemInformBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.view.BaseRvViewHolder

/**
 * 适配系统通知布局
 * create by 何姝霖
 */
class SystemInformAdapter(context: Context,
                          mDatas: List<SystemInformBean>) :
    BaseRvAdapter<SystemInformBean, SystemInformViewHolder>(
        context,
        mDatas as MutableList<SystemInformBean>) {

    override fun onBindData(holder: SystemInformViewHolder, data: SystemInformBean, position: Int) {
        holder.tvTitle.text = data.title
        holder.tvTime.text = data.createTime
        holder.tvContent.text = data.text
        val layoutParams = holder.root.getLayoutParams() as ViewGroup.MarginLayoutParams
        if (position == mDatas.size - 1) {
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.rightMargin,
                CommonUtil.dp2px(context, 30)
            )
        } else {
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.rightMargin,
                0
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemInformViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_system_inform, parent, false)
        return SystemInformViewHolder(view)
    }

    inner class SystemInformViewHolder(itemView: View?) : BaseRvViewHolder(itemView) {
        val tvTitle: TextView = itemView!!.findViewById(R.id.tv_system_inform_title)
        val tvTime: TextView = itemView!!.findViewById(R.id.tv_system_inform_time)
        val tvContent: TextView = itemView!!.findViewById(R.id.tv_system_inform_content)
        val root : View = itemView!!.findViewById(R.id.root)
    }
}