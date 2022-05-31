package com.qunshang.wenpaitong.equnshang.activity

import android.graphics.Typeface

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_amall_search_result_v3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.AMallAdapterV3
import com.qunshang.wenpaitong.equnshang.data.AMallV3SearchProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class AMallSearchResultV3Activity : BaseActivity() {
    var keyword: String? = null

    var categoryId: Int? = null

    var manufacturerId: Int? = null

    var istop = false

    var pageIndex = 1

    val pageSize = 20

    var order = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amall_search_result_v3)
        keyword = intent.getStringExtra("keyword")
        StringUtils.log("当前的信息是" + keyword)
        if (!StringUtils.isEmpty(keyword)){
            input.visibility = View.GONE
            layout_text.visibility = View.VISIBLE
            text_input.setText(keyword)
        } else {
            input.visibility = View.VISIBLE
            layout_text.visibility = View.GONE
            text_input.setText("")
        }
        layout_text.setOnClickListener {
            input.visibility = View.VISIBLE
            layout_text.visibility = View.GONE
            text_input.setText("")
        }
        categoryId = intent.getIntExtra("categoryId",-9999)
        if (categoryId == -9999){
            categoryId = null
        }
        manufacturerId = intent.getIntExtra("manufacturerId",-9999)
        if (manufacturerId == -9999){
            manufacturerId = null
        }
        input.setOnKeyListener { v, keyCode, event ->
            if(event.getAction()== KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    if (input.text.toString().length != 0){
                        categoryId = null
                        manufacturerId = null
                        keyword = input.text.toString()
                        pageIndex = 1
                        adapter.clearData()
                        searchProducts()
                    }
                } else if (keyCode == KeyEvent.KEYCODE_DEL){
                    if (input.text.length != 0){
                        input.text.delete(input.text.length - 1, input.text.length)
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish()
                }
            }

            return@setOnKeyListener true
        }
        initView()
    }

    var index = 0

    fun initView() {
        layout_def.setOnClickListener {
            if (index != 0){
                index = 0
                text_def.setTypeface(null, Typeface.BOLD)
                label_def.visibility = View.VISIBLE
                text_new.setTypeface(null, Typeface.NORMAL)
                label_new.visibility = View.GONE
                text_sale.setTypeface(null, Typeface.NORMAL)
                label_sale.visibility = View.GONE
                text_price.setTypeface(null, Typeface.NORMAL)
                label_price_top.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                this.order = 0
                if (this::adapter.isInitialized){
                    adapter.clearData()
                }
                pageIndex = 1
                searchProducts()
            }
        }
        layout_new.setOnClickListener {
            if (index != 1){
                index = 1
                text_def.setTypeface(null, Typeface.NORMAL)
                label_def.visibility = View.GONE
                text_new.setTypeface(null, Typeface.BOLD)
                label_new.visibility = View.VISIBLE
                text_sale.setTypeface(null, Typeface.NORMAL)
                label_sale.visibility = View.GONE
                text_price.setTypeface(null, Typeface.NORMAL)
                label_price_top.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                this.order = 2
                if (this::adapter.isInitialized){
                    adapter.clearData()
                }
                pageIndex = 1
                searchProducts()
            }
        }
        layout_sale.setOnClickListener {
            if (index != 2){
                index = 2
                text_def.setTypeface(null, Typeface.NORMAL)
                label_def.visibility = View.GONE
                text_new.setTypeface(null, Typeface.NORMAL)
                label_new.visibility = View.GONE
                text_sale.setTypeface(null, Typeface.BOLD)
                label_sale.visibility = View.VISIBLE
                text_price.setTypeface(null, Typeface.NORMAL)
                label_price_top.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                this.order = 1
                if (this::adapter.isInitialized){
                    adapter.clearData()
                }
                pageIndex = 1
                searchProducts()
            }
        }
        layout_price.setOnClickListener {
            index = 3
            text_def.setTypeface(null, Typeface.NORMAL)
            label_def.visibility = View.GONE
            text_new.setTypeface(null, Typeface.NORMAL)
            label_new.visibility = View.GONE
            text_sale.setTypeface(null, Typeface.NORMAL)
            label_sale.visibility = View.GONE
            text_price.setTypeface(null, Typeface.NORMAL)
            if (istop){
                istop = false
                label_price_top.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_selected))
                label_price_bottom.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                this.order = 3
                if (this::adapter.isInitialized){
                    adapter.clearData()
                }
                pageIndex = 1
                searchProducts()
            } else {
                istop = true
                label_price_top.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(this.resources.getColor(R.color.color_amallv3_product_detail_top_selected))
                this.order = 4
                if (this::adapter.isInitialized){
                    adapter.clearData()
                }
                pageIndex = 1
                searchProducts()
            }
        }
        cancel.setOnClickListener {
            finish()
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = manager.childCount
                    var totalItemCount = manager.itemCount
                    StringUtils.log("大年可见" + totalItemCount)
                    if (!isloading && lastVisibleItemPosition == totalItemCount - 1) {
                        isloading = true
                        searchProducts()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = manager.findLastVisibleItemPosition()
            }
        })
        list.layoutManager = manager
        adapter = AMallAdapterV3(this@AMallSearchResultV3Activity)
        list.adapter = adapter
        searchProducts()
    }

    lateinit var adapter : AMallAdapterV3

    var isloading = false

    var isAutoLoadMoreEnabled = true

    private var lastVisibleItemPosition = 0

    fun searchProducts() {
        isAutoLoadMoreEnabled = true
        ApiManager.getInstance().getApiAMallV3()
            .searchAMallV3(categoryId = categoryId, pageIndex = pageIndex, pageSize = pageSize,
                manufacturerId = manufacturerId,
                keyword = keyword,sortBy = order)
            .enqueue(object : Callback<AMallV3SearchProductBean> {
                override fun onResponse(
                    call: Call<AMallV3SearchProductBean>,
                    response: Response<AMallV3SearchProductBean>
                ) {
                    isloading = false
                    if (response.body() == null) {
                        list.visibility = View.GONE
                        layout_epmty.visibility = View.VISIBLE
                        return
                    }


                    if ((response.body()!!.data.productList == null && pageIndex == 1)){
                        list.visibility = View.GONE
                        layout_epmty.visibility = View.VISIBLE
                        return
                    }
                    if ((response.body()!!.data.productList == null)){
/*                        list.visibility = View.GONE
                        layout_epmty.visibility = View.VISIBLE*/
                        return
                    }
                    if (response.body()!!.data.productList.size == 0 && pageIndex == 1){
                        list.visibility = View.GONE
                        layout_epmty.visibility = View.VISIBLE
                        return
                    }
                    if (response.body()!!.data.productList.size < pageSize){
                        isAutoLoadMoreEnabled = false
                        adapter.showisShowBottomLine(true)
                    }
                    list.visibility = View.VISIBLE
                    layout_epmty.visibility = View.GONE
                    adapter.addData(response.body()!!.data.productList)
                    this@AMallSearchResultV3Activity.pageIndex++

                }

                override fun onFailure(call: Call<AMallV3SearchProductBean>, t: Throwable) {

                }

            })

    }
}