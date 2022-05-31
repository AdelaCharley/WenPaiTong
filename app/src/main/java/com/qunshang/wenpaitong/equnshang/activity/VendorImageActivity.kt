package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.qunshang.wenpaitong.R
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_vendor_image.*
import kotlinx.android.synthetic.main.activity_vendor_image.list
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_back
import kotlinx.android.synthetic.main.layout_toolbar.toolbar_title
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.VendorImageAdatper
import com.qunshang.wenpaitong.equnshang.data.VendorPictureBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager

class VendorImageActivity : BaseActivity() {

    var vendorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_image)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("机构图册")
        spinner.setDropDownWidth(400) //下拉宽度
        vendorId = intent.getIntExtra("vendorId",0)
        spinner.setDropDownHorizontalOffset(100) //下拉的横向偏移

        spinner.setDropDownVerticalOffset(100) //下拉的纵向偏移

        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效


        //mSpinnerSimple.setBackgroundColor(AppUtil.getColor(instance,R.color.wx_bg_gray)); //下拉的背景色
        //spinner mode ： dropdown or dialog , just edit in layout xml
        //mSpinnerSimple.setPrompt("Spinner Title"); //弹出框标题，在dialog下有效
        val spinnerItems = arrayOf("默认排序", "时间升序", "时间倒序")
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        val spinnerAdapter = ArrayAdapter(
            this,
            R.layout.item_spinner_selected, spinnerItems
        )
        //自定义下拉的字体样式
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_drop)
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        spinner.setAdapter(spinnerAdapter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0){
                    loadImages(vendorId,null)
                } else if (position == 1) {
                    loadImages(vendorId,"asc")
                } else if (position == 2){
                    loadImages(vendorId,"desc")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        loadImages(vendorId,null)
    }

    fun loadImages(vendorId : Int,order : String?){
        ApiManager.getInstance().getApiStore().loadVendorPictures(vendorId,timeOrder = order).enqueue(object :
            Callback<VendorPictureBean> {
            override fun onResponse(
                call: Call<VendorPictureBean>,
                response: Response<VendorPictureBean>
            ) {

                if (response.body() == null){
                    return
                }
                piccount.setText("共" + response.body()!!.data.size + "张")
                list.adapter = VendorImageAdatper(this@VendorImageActivity,response.body()!!.data)
            }

            override fun onFailure(call: Call<VendorPictureBean>, t: Throwable) {

            }

        })
    }

}