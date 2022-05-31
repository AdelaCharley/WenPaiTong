package com.qunshang.wenpaitong.equnshang.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import de.hdodenhof.circleimageview.CircleImageView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfo


class PinTuanProgressAdapter(
    private val is4Back1: Boolean,
    private val userInfo: List<UserInfo>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeNormal = 0
    private val typeBack1 = 1

    override fun getItemViewType(position: Int): Int = if (is4Back1) typeBack1 else typeNormal

    override fun getItemCount(): Int = if (is4Back1) 4 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == typeBack1)
            OwnerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_pin_tuan_progress_back1, parent, false)
            )
        else NormalViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pin_tuan_progress_normal, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OwnerViewHolder) {
            initLayout(holder, position)
            var userSize = userInfo.size
            if (position == 0){
                holder.sign.visibility = View.VISIBLE
            }
            if (position < userSize) {
                holder.head.load(userInfo[position].headImage)
                holder.head.borderColor = Color.parseColor("#F3594F")
                holder.head.borderWidth = 1
                holder.lineLeft.setBackgroundColor(Color.parseColor("#F3594F"))
                if (position == 1 || position == 3) {
                    holder.lineCenter.setBackgroundColor(Color.parseColor("#F3594F"))
                    if (position == 1) {
                        holder.lineRight.setBackgroundColor(Color.parseColor("#F3594F"))
                    }
                }
            }
            if ((userSize < 2 && position == 1) || (userSize < 4 && position == 3)) {
                holder.nodeFinished.visibility = View.GONE
            }
        } else if (holder is NormalViewHolder) {
            holder.headLeft.load(userInfo[0].headImage)
            holder.headLeft.borderColor = Color.parseColor("#F3594F")
            holder.headLeft.borderWidth = 1
            if (userInfo.size == 2) {
                holder.headRight.load(userInfo[1].headImage)
                holder.headRight.borderColor = Color.parseColor("#F3594F")
                holder.headRight.borderWidth = 1
            }
        }
    }

    private fun initLayout(holder: OwnerViewHolder, position: Int) {
        if (is4Back1) {
            when (position) {
                1 -> holder.content.text = "两人成团"
                3 -> {
                    holder.content.text = "四人免单"
                    holder.lineRight.visibility = View.GONE
                }
                else -> {
                    holder.lineCenter.visibility = View.GONE
                    holder.lineRight.visibility = View.GONE
                    holder.nodeDefault.visibility = View.GONE
                    holder.nodeFinished.visibility = View.GONE
                    holder.content.visibility = View.GONE
                }
            }
        } else {
            holder.lineLeft.visibility = View.GONE
            holder.lineCenter.visibility = View.GONE
            holder.lineRight.visibility = View.GONE
            holder.nodeDefault.visibility = View.GONE
            holder.nodeFinished.visibility = View.GONE
            holder.content.visibility = View.GONE
        }

    }

    // 四返一
    inner class OwnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lineLeft: View = itemView.findViewById(R.id.line_left)
        var lineCenter: View = itemView.findViewById(R.id.line_center)
        var lineRight: View = itemView.findViewById(R.id.line_right)
        var head: CircleImageView = itemView.findViewById(R.id.head)
        var sign: TextView = itemView.findViewById(R.id.tv_sign_owner)
        var nodeDefault: ImageView = itemView.findViewById(R.id.node_default)
        var nodeFinished: ImageView = itemView.findViewById(R.id.node_finished)
        var content: TextView = itemView.findViewById(R.id.node_content)
    }

    // 非四返一
    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headLeft: CircleImageView = itemView.findViewById(R.id.head_left)
        var headRight: CircleImageView = itemView.findViewById(R.id.head_right)
    }
}