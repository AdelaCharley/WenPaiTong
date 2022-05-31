package com.qunshang.wenpaitong.equnshang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.greenrobot.eventbus.EventBus
import com.qunshang.wenpaitong.R

class WenBanTongDownloadMaterialsAdapter(private val context: Context, private var allMaterials: List<Any>): RecyclerView.Adapter<WenBanTongDownloadMaterialsAdapter.ViewHolder>() {
    private var selectedMaterials: MutableList<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_wbt_download_materials, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = allMaterials[position]
        if (currentData is String){
            holder.image.load(currentData)
            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    selectedMaterials.add(currentData)
                }else{
                    selectedMaterials.remove(currentData)
                }
                /**
                 * subscribe:[com.qunshang.wenpaitong.equnshang.activity.WenBanTongDownloadMaterialsActivity]
                 */
                EventBus.getDefault().post(selectedMaterials)
            }
            holder.itemView.setOnClickListener{
                holder.checkBox.isChecked = !holder.checkBox.isChecked
            }
            holder.checkBox.isChecked = currentData in selectedMaterials
        } else if (currentData is Bitmap){
            holder.image.load(currentData)
            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    selectedMaterials.add(currentData)
                }else{
                    selectedMaterials.remove(currentData)
                }
                /**
                 * subscribe:[com.qunshang.wenpaitong.equnshang.activity.WenBanTongDownloadMaterialsActivity]
                 */
                EventBus.getDefault().post(selectedMaterials)
            }
            holder.itemView.setOnClickListener{
                holder.checkBox.isChecked = !holder.checkBox.isChecked
            }
            holder.checkBox.isChecked = currentData in selectedMaterials
        }

    }

    override fun getItemCount(): Int = allMaterials.size

    /**
     * 完成下载后，清除勾选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearSelected(){
        selectedMaterials = mutableListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image:ImageView = itemView.findViewById(R.id.image)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }
}