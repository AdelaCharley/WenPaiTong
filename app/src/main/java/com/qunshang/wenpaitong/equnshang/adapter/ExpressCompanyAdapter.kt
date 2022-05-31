package com.qunshang.wenpaitong.equnshang.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.ExpressCompanyBean

//interface OnItemClickListener{
//    fun onItemClick(v: View, position: Int)
//}

class ExpressCompanyAdapter(private val context: Context, private val data: List<ExpressCompanyBean>) : RecyclerView.Adapter<ExpressCompanyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    = ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.item_express_company, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = data[position].companyName
        val code = data[position].code
        holder.company.text = name
        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.putExtra("companyName", name)
            intent.putExtra("companyCode", code)
            val activity: Activity = context as Activity
            activity.setResult(RESULT_CODE,intent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val company: TextView = itemView.findViewById(R.id.tv_express_company)
    }

    companion object {
        const val RESULT_CODE = 321
    }


}