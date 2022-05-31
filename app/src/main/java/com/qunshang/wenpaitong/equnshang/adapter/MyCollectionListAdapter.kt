package com.qunshang.wenpaitong.equnshang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.Constants
import com.qunshang.wenpaitong.equnshang.activity.MyCollectionItemBean
import com.qunshang.wenpaitong.equnshang.activity.V300PublisherMainHomeActivity
import com.qunshang.wenpaitong.equnshang.activity.WebViewActivity
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class MyCollectionListAdapter(private val activityContext: Context):
    RecyclerView.Adapter<MyCollectionListAdapter.ViewHolder>() {
    private var datas: MutableList<MyCollectionItemBean> = mutableListOf()
    private var selectedIds: MutableList<Int> = mutableListOf()
    private var isItemSelected = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_college_new_list, parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = datas[position]
        /* 复选框 */
        holder.cbSelect.visibility = if (isItemSelected) View.VISIBLE else View.GONE
        holder.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedIds.add(currentData.collectId)
            } else {
                selectedIds.remove(currentData.collectId)
            }
            EventBus.getDefault().post("删除（${selectedIds.size}）")
        }
        holder.cbSelect.isChecked = currentData.collectId in selectedIds
        StringUtils.log("当前pos是" + position + " Ids " + selectedIds + "当前Id" + currentData.collectId + "判断结果" + (currentData.collectId in selectedIds))
        /* 发布者信息 */
        val agency = currentData.agency
        holder.imgHead.load(agency.agencyAvatar)
        holder.tvName.text = agency.agencyName
        /* 文章信息 */
        val news = currentData.news
        val posters = news.newsPoster
        holder.tvTime.text = news.createTime
        holder.tvTitle.text = news.title
        holder.imgContent0.visibility = if (posters.size == 1) View.VISIBLE else View.GONE
        holder.layoutBottomImg.visibility = if (posters.size == 3) View.VISIBLE else View.GONE
        when (posters.size) {
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
        /* 跳转事件 */
        holder.rootHead.setOnClickListener {
            if (!isItemSelected) {
                val intent =
                    Intent(activityContext, V300PublisherMainHomeActivity::class.java)
                intent.putExtra("agencyId", agency.agencyId)
                activityContext.startActivity(intent)
            }
        }
        holder.root.setOnClickListener {
            if (!isItemSelected) {
                val intent = Intent(activityContext, WebViewActivity::class.java)
                intent.putExtra("title", "")
                intent.putExtra(
                        "url",
                        Constants.baseURL + "/equnshang/business/news?userId=" + UserInfoViewModel.getUserId() + "&newsId=" + news.newsId
                )
                activityContext.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = datas.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(data: List<MyCollectionItemBean>?) {
        if (data != null) {
            datas.addAll(data)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(isAll: Int?) {
        if (isAll == 1) {
            datas.clear()
        } else {
            val tmpData:MutableList<MyCollectionItemBean> = mutableListOf()
            for (d in datas) {
                if (d.collectId !in selectedIds) {
                    tmpData.add(d)
                }
            }
            datas = tmpData
        }
        selectedIds.clear()
        notifyDataSetChanged()
    }

    fun getSelectedId() = selectedIds.toIntArray()

    @SuppressLint("NotifyDataSetChanged")
    fun toSelect() {
        selectedIds.clear()
        isItemSelected = !isItemSelected
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val root: View = itemView.findViewById(R.id.root)
        val rootHead: View = itemView.findViewById(R.id.in_head)

        val cbSelect: CheckBox = itemView.findViewById(R.id.cb_select)
        //上：发布信息
        val imgHead: CircleImageView = itemView.findViewById(R.id.img_head)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        //中：文章内容
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val imgContent0: ShapeableImageView = itemView.findViewById(R.id.img_content0)  //位于右侧的图
        val layoutBottomImg: LinearLayout =
            itemView.findViewById(R.id.layout_bottom_img) //位于下方的3张图
        val imgContent1: ShapeableImageView = itemView.findViewById(R.id.img_content1)
        val imgContent2: ShapeableImageView = itemView.findViewById(R.id.img_content2)
        val imgContent3: ShapeableImageView = itemView.findViewById(R.id.img_content3)
        //下：文章数据
        val tvRead: TextView = itemView.findViewById(R.id.tv_read)
        val tvComment: TextView = itemView.findViewById(R.id.tv_comment)
        val tvLike: TextView = itemView.findViewById(R.id.tv_like)
    }

}