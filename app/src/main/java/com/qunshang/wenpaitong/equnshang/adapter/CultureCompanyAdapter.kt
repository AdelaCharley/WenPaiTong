package com.qunshang.wenpaitong.equnshang.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongCompanyDetailActivity
import com.qunshang.wenpaitong.equnshang.data.CultureCompanyBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 *
 * create by 何姝霖
 */
class CultureCompanyAdapter : RecyclerView.Adapter<CultureCompanyAdapter.ViewHolder>() {

    var data: MutableList<CultureCompanyBean>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_culture_company, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        StringUtils.log("saaffjdkjfdkjfkdmfdl")
        holder.logo.load(data!![position].companyLogo)
        holder.name.text = data!![position].companyName
        holder.poster.load(data!![position].companyPoster)
        holder.layout.setOnClickListener {
            /*if (!UserHelper.isLogin(holder.layout.context)) {
                holder.layout.context.startActivity(Intent(holder.layout.context, LoginActivity::class.java))
                return@setOnClickListener
            }*/
            val intent = Intent(holder.layout.context,WenBanTongCompanyDetailActivity::class.java)
            intent.putExtra("companyId",data!![position].companyId)
            holder.layout.context.startActivity(intent)
        }
        val layoutParams = holder.layout.layoutParams as MarginLayoutParams
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
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun add(data: List<CultureCompanyBean>) {
        if (this.data != null) {
            this.data!!.addAll(data)
        } else {
            this.data = data.toMutableList()
        }
        Log.d("shulinr", "一共有${data.size}个item")
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout : ViewGroup = itemView.findViewById(R.id.layout)
        val logo: ShapeableImageView = itemView.findViewById(R.id.company_logo)
        val name: TextView = itemView.findViewById(R.id.company_name)
        val poster: ShapeableImageView = itemView.findViewById(R.id.company_poster)
        //val layout : View = itemView.findViewById(R.id.layout)
    }
}