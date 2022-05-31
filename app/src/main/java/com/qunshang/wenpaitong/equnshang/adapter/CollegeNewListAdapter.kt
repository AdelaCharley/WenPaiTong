package com.qunshang.wenpaitong.equnshang.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import de.hdodenhof.circleimageview.CircleImageView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.activity.V300PublisherMainHomeActivity
import com.qunshang.wenpaitong.equnshang.activity.WebViewActivity
import com.qunshang.wenpaitong.equnshang.data.CollegeNewItemBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class CollegeNewListAdapter :
    RecyclerView.Adapter<CollegeNewListAdapter.ViewHolder>() {
    private var datas: MutableList<CollegeNewItemBean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_college_new_list, parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agency = datas!![position].agency
        holder.imgHead.load(agency.agencyAvatar)
        holder.tvName.text = agency.agencyName

        val news = datas[position].news
        val posters = news.newsPoster
        holder.tvTime.text = news.createTime
        holder.tvTitle.text = news.title
        holder.imgContent0.visibility = if (posters?.size == 1) View.VISIBLE else View.GONE
        holder.layoutBottomImg.visibility = if (posters?.size == 3) View.VISIBLE else View.GONE
        when (posters?.size) {
            1 -> {
                holder.imgContent0.load(posters[0])
            }
            3 -> {
                holder.imgContent1.load(posters[0])
                holder.imgContent2.load(posters[1])
                holder.imgContent3.load(posters[2])
            }
        }
        holder.tvRead.text = "${news.browserNum}阅读"
        holder.tvComment.text = "${news.commentNum}评论"
        holder.tvLike.text = "${news.upNum}点赞"
        holder.root_head.setOnClickListener {
            if (!UserHelper.isLogin(holder.root.context)) {
                holder.root.context.startActivity(Intent(holder.root.context, LoginActivity::class.java))
                return@setOnClickListener
            }
            val intent = Intent(holder.imgHead.context,V300PublisherMainHomeActivity::class.java)
            intent.putExtra("agencyId",agency.agencyId)
            holder.imgHead.context.startActivity(intent)
        }
        holder.root.setOnClickListener {
            /*if (!UserHelper.isLogin(holder.imgHead.context)) {
                holder.imgHead.context.startActivity(Intent(holder.imgHead.context, LoginActivity::class.java))
                return@setOnClickListener
            }*/
            val intent = Intent(holder.root.context, WebViewActivity::class.java)
            intent.putExtra("title","")
            intent.putExtra("bigtitle",news.title)
            intent.putExtra("subtitle",news.subTitle)
            if (news.newsPoster == null){
                intent.putExtra("shareimage",agency.agencyAvatar)
            } else {
                if (news.newsPoster.size > 0){
                    intent.putExtra("shareimage",news.newsPoster.get(0))
                } else {
                    intent.putExtra("shareimage", agency.agencyAvatar)
                }
            }
            if (!UserHelper.isLogin(holder.root.context)){
                intent.putExtra("url", Constants.baseURL + "/equnshang/business/news?" + "newsId=" + datas[position].news.newsId)
            } else {
                intent.putExtra("url", Constants.baseURL + "/equnshang/business/news?userId=" + UserInfoViewModel.getUserId() + "&newsId=" + datas[position].news.newsId)
            }
            holder.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =  datas.size

    fun clear(){
        if(datas != null){
            datas.removeAll(datas)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(data: List<CollegeNewItemBean>?) {
        if (data != null) {
            datas.addAll(data)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root : View = itemView.findViewById(R.id.root)
        val root_head = itemView.findViewById<View>(R.id.in_head)
        //上：发布信息
        val imgHead: CircleImageView = itemView.findViewById(R.id.img_head)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        //中：文章内容
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val imgContent0: ShapeableImageView = itemView.findViewById(R.id.img_content0)  //位于右侧的图
        val layoutBottomImg: LinearLayout = itemView.findViewById(R.id.layout_bottom_img) //位于下方的3张图
        val imgContent1: ShapeableImageView = itemView.findViewById(R.id.img_content1)
        val imgContent2: ShapeableImageView = itemView.findViewById(R.id.img_content2)
        val imgContent3: ShapeableImageView = itemView.findViewById(R.id.img_content3)
        //下：文章数据
        val tvRead: TextView = itemView.findViewById(R.id.tv_read)
        val tvComment: TextView = itemView.findViewById(R.id.tv_comment)
        val tvLike: TextView = itemView.findViewById(R.id.tv_like)
    }

}