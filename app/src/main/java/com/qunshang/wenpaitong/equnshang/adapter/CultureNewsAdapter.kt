package com.qunshang.wenpaitong.equnshang.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.activity.WebViewActivity
import com.qunshang.wenpaitong.equnshang.data.CultureNewsBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 *
 * create by 何姝霖
 */
class CultureNewsAdapter : RecyclerView.Adapter<CultureNewsAdapter.ViewHolder>() {

    var data: MutableList<CultureNewsBean>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_culture_news, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.poster.load(data!![position].newsPoster)
        holder.title.text = data!![position].title
        holder.time.text = data!![position].createTime
        Glide.with(holder.avatar.context).load(data!![position].companyAvatar).into(holder.avatar)
        holder.company_name.setText(data!![position].companyName)
        val layoutParams = holder.layout.layoutParams as ViewGroup.MarginLayoutParams
        if (position == data!!.size - 1) {
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.rightMargin,
                CommonUtil.dp2px(holder.layout.context, 30)
            )
        } else {
            layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin,
                layoutParams.rightMargin,
                0
            )
        }
        holder.layout.setOnClickListener {
            /*if (!UserHelper.isLogin(holder.layout.context)) {
                holder.layout.context.startActivity(Intent(holder.layout.context, LoginActivity::class.java))
                return@setOnClickListener
            }*/
            val intent = Intent(holder.layout.context,WebViewActivity::class.java)
            intent.putExtra("title","资讯详情")
            intent.putExtra("url", Constants.baseURL + "/equnshang/culture/article?id=" + data!![position].newsId)
            holder.layout.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun add(data: List<CultureNewsBean>) {
        if (this.data != null) {
            this.data!!.addAll(data)
        } else {
            this.data = data.toMutableList()
        }
        Log.d("shulinr", "一共有${data.size}个item")
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar : ImageView = itemView.findViewById(R.id.companyavatar)
        val company_name : TextView = itemView.findViewById(R.id.company_name)
        val poster: ShapeableImageView = itemView.findViewById(R.id.news_poster)
        val title: TextView = itemView.findViewById(R.id.news_title)
        val time: TextView = itemView.findViewById(R.id.news_time)
        val layout : ViewGroup = itemView.findViewById(R.id.layout)
    }
}