package com.qunshang.wenpaitong.equnshang.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.*
import com.qunshang.wenpaitong.equnshang.data.BottomIcon

class PersonalBottomIconAdapter(val context: Context, val data: List<BottomIcon>) :
    RecyclerView.Adapter<PersonalBottomIconAdapter.ViewHolder>() {
    private val TAG = "shu-PersonalIcon"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_copyright_personal_bottom, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.load(data[position].image)
        holder.title.text = data[position].title

        var intent : Intent ? = null
        when(data[position].key) {
            "order" -> {
                intent = Intent(context,WenBanTongOrderListActivity::class.java)
            }
            "people" -> {
                intent = Intent(context,NewRenMaiActivity::class.java)
            }
            "credit" -> {
                intent = Intent(context,MyZiChanActivity::class.java)
                intent.putExtra("page",0)
            }
            "assets" -> {
                intent = Intent(context,MyZiChanActivity::class.java)
                intent.putExtra("page",0)
            }
            "money" -> {
                intent = Intent(context,MyZiChanActivity::class.java)
                intent.putExtra("page",2)
            }
            "wbt" -> {
                intent = Intent(context,MyZiChanActivity::class.java)
                intent.putExtra("page",3)
            }
            "o2o" -> {
                intent = Intent(context,MyPhysicalStoresActivity::class.java)
            }
            "feedback" -> {
                intent = Intent(context,FeedBackActivity::class.java)
            }
            "setting" -> {
                intent = Intent(context,AccountSettingActivity::class.java)
            }
            "service" -> {
                intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + context.resources.getString(R.string.companyphone)))
                //context.startActivity(intent)
            }
            else -> {
                intent = Intent(context,AccountSettingActivity::class.java)
            }
        }
        holder.layout.setOnClickListener {
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout = itemView.findViewById(R.id.layout)
        val icon: ImageView = itemView.findViewById(R.id.img_icon)
        val title: TextView = itemView.findViewById(R.id.tv_title)
    }
}