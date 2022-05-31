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
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongProductDetailActivity
import com.qunshang.wenpaitong.equnshang.data.CultureProductBean
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 *
 * create by 何姝霖
 */
class CultureProductAdapter(val isShowBanner : Boolean = true) : RecyclerView.Adapter<CultureProductAdapter.ViewHolder>() {

    val typeNormal = 0
    val typeDiscount = 1
    val typeBanner = 2
    var data: MutableList<CultureProductBean>? = null

    /*lateinit var bannerurls : List<String>

    *//*override fun getItemViewType(position: Int): Int = if (data?.get(position)?.discount == null) typeNormal else typeDiscount*//*

    fun setBanner(bannerurls : List<String>){
        this.bannerurls = bannerurls
        notifyItemChanged(0)
        StringUtils.log("耳机覅")
    }*/

    /*override fun getItemViewType(position: Int): Int {
        if (position == 0){
            return typeBanner
        }
        if (data?.get(position - 1)?.discount == null) return typeNormal else return typeDiscount
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return NormalViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_new_wenbantong_product, parent, false)
        )/*
        if (viewType == typeNormal) {
            return NormalViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_culture_product, parent, false)
            )
        } else if (viewType == typeBanner){
            return BannerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_wenbantong_banner,parent,false)
            )
        } else {
            return DiscountViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_culture_product_discount, parent, false)
            )
        }*/
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = position
        /*if (holder is NormalViewHolder) {
            holder.tvName.text = data!![pos].productName
            holder.tvPrice.text = data!![pos].productPrice
            holder.tvTag.text = data!![pos].tag
            holder.tvUnsold.text = "将于${data!![pos].startTime}开售"

            //status: 10-未开售，20-进行中，30-已结束
            val status = data!![pos].status
            if (status == "30") {
                holder.imgPoster.load(data!![pos].productPoster) {
                    transformations(GrayscaleTransformation())
                }
                holder.tvName.setTextColor(Color.parseColor("#BFBDB8"))
                holder.tvEnd.visibility = View.VISIBLE
                holder.layoutPrice.visibility = View.GONE
            } else {
                holder.imgPoster.load(data!![pos].productPoster)
                holder.tvName.setTextColor(Color.parseColor("#312E2A"))
                holder.tvEnd.visibility = View.GONE
                holder.layoutPrice.visibility = View.VISIBLE
            }
            holder.tvUnsold.visibility = if (status == "10") View.VISIBLE else View.GONE
            holder.layout.setOnClickListener {
                val intent =
                    Intent(holder.layout.context, WenBanTongProductDetailActivity::class.java)
                intent.putExtra("productId", data!![pos].productId)
                holder.layout.context.startActivity(intent)
                Log.d("shulinr", "status: ${data!![pos].status}")
            }
        }*/
        if (holder is NormalViewHolder) {
            holder.imgPoster.load(data!![pos].productPoster)
            holder.tvName.text = data!![pos].productName
            if (data!![pos].discount == null){
                holder.tvPrice.text = data!![pos].productPrice
            } else {
                holder.tvPrice.text = data!![pos].discount?.productDiscountPriceStr
            }

            holder.tvTag.text = data!![pos].tag
            //holder.tvUnsold.text = "将于${data!![pos].startTime}开售"
            holder.name.setText(data!![pos].companyName)
            //status: 10-未开售，20-进行中，30-已结束
            val status = data!![pos].status.toInt()
            if (status == 10){
                holder.tvUnsold.setText("敬请期待 " + data!![pos].startTime + "开售")
            } else if (status == 20){
                holder.tvUnsold.setText("正在热销")
            } else {
                holder.tvUnsold.setText("已售罄")
            }
            holder.stock.setText(data!![pos].productInitStock.toString() + "份")
            Glide.with(holder.companyAvatar.context).load(data!![pos].companyAvatar).into(holder.companyAvatar)
            holder.layout.setOnClickListener {
                /*if (!UserHelper.isLogin(holder.layout.context)) {
                    holder.layout.context.startActivity(Intent(holder.layout.context, LoginActivity::class.java))
                    return@setOnClickListener
                }*/
                val intent =
                    Intent(holder.layout.context, WenBanTongProductDetailActivity::class.java)
                intent.putExtra("productId", data!![pos].productId)
                holder.layout.context.startActivity(intent)
                Log.d("shulinr", "status: ${data!![pos].status}")
            }
            holder.originalprice.setText(data?.get(pos)?.evaluationPrice)
            val layoutParams = holder.tvTag.layoutParams as ViewGroup.MarginLayoutParams
            if (data!![pos].discount != null){
                holder.lable_discount.setText(data!![pos].discount?.discountStr)
                holder.lable_discount.width = CommonUtil.dp2px(holder.lable_discount.context,52)

                layoutParams.setMargins(CommonUtil.dp2px(holder.tvTag.context,5),layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin)
            } else {
                holder.lable_discount.setText("")
                holder.lable_discount.width = 0
                layoutParams.setMargins(CommonUtil.dp2px(holder.tvTag.context,0),layoutParams.topMargin,layoutParams.rightMargin,layoutParams.bottomMargin)
            }
        }

        val layoutParams = holder.layout.layoutParams as ViewGroup.MarginLayoutParams
        if (position == (data?.size!! - 1)) {
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

    /**
     * 在有折扣时设置商品名首行缩进
     */
    /*private fun setDiscountProductName(
        context: Context,
        discountStrLength: Int,
        productName: String
    ): SpannableStringBuilder {
        val tvNameBuilder = SpannableStringBuilder(productName)
        val end = tvNameBuilder.length
        val flags = SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE
        val marginPx = (discountStrLength + 2) * CommonUtil.dp2px(context, 10)
        val leadingMarginSpan = LeadingMarginSpan.Standard(marginPx, 0)
        tvNameBuilder.setSpan(leadingMarginSpan, 0, end, flags)
        return tvNameBuilder
    }*/

    override fun getItemCount(): Int {
        /*if (data == null){
            return 1
        }
        return data!!.size + 1*/
        if (data == null){
            return 0
        }
        return data!!.size
    }

    //override fun getItemCount(): Int = (data?.size + 1) ?: 0

    fun add(data: List<CultureProductBean>) {
        if (this.data != null) {
            this.data!!.addAll(data)
        } else {
            this.data = data.toMutableList()
        }
        Log.d("shulinr", "一共有${data.size}个item")
        notifyDataSetChanged()
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: ViewGroup = itemView.findViewById(R.id.layout)
    }

    /*inner class BannerViewHolder(itemView: View) : ViewHolder(itemView){
        var banner: Banner<String, BannerImageAdapter<String>> = itemView.findViewById(R.id.banner)
    }*/

    inner class NormalViewHolder(itemView: View) : ViewHolder(itemView) {
        /*val imgPoster: ShapeableImageView = itemView.findViewById(R.id.img_product_poster)
        val tvName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val tvTag: TextView = itemView.findViewById(R.id.tv_tag)

        val tvUnsold: TextView = itemView.findViewById(R.id.tv_state_unsold)
        val tvEnd: TextView = itemView.findViewById(R.id.tv_state_end)
        val layoutPrice: LinearLayout = itemView.findViewById(R.id.layout_price)*/
        val imgPoster: ShapeableImageView = itemView.findViewById(R.id.image_product)
        val tvName: TextView = itemView.findViewById(R.id.text_new_wenbantong_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val tvTag: TextView = itemView.findViewById(R.id.labelnew_wenbantong_count)
        val companyAvatar = itemView.findViewById<ImageView>(R.id.companyavatar)
        val tvUnsold: TextView = itemView.findViewById(R.id.tv_state_unsold)
        //val tvEnd: TextView = itemView.findViewById(R.id.tv_state_end)
        //val layoutPrice: LinearLayout = itemView.findViewById(R.id.layout_price)

        val stock : TextView = itemView.findViewById(R.id.label_new_wenbantong_stock)

        val lable_discount : TextView = itemView.findViewById(R.id.label_discount)
        val name : TextView = itemView.findViewById(R.id.company_name)
        val originalprice : TextView = itemView.findViewById(R.id.originalprice)
    }

    /*inner class DiscountViewHolder(itemView: View) : ViewHolder(itemView) {
        val imgPoster: ShapeableImageView = itemView.findViewById(R.id.img_product_poster)
        val tvDiscount: TextView = itemView.findViewById(R.id.tv_discount_name)
        val tvName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvDelPrice: TextView = itemView.findViewById(R.id.tv_del_price)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val tvTag: TextView = itemView.findViewById(R.id.tv_tag)
    }*/
}