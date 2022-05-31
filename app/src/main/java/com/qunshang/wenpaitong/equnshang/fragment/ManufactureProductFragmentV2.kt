package com.qunshang.wenpaitong.equnshang.fragment

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_manufacture_product_v2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.ManufactureProductAdapterV2
import com.qunshang.wenpaitong.equnshang.data.AMallV3SearchProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ManufactureProductFragmentV2(val manufactureId : Int) : MyBaseFragment() {

    var pageIndex = 1

    var pageSize = 20

    var index = 0

    var istop = false

    var orderType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manufacture_product_v2, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = GridLayoutManager(context,2)
        products.layoutManager = manager
        adapterV2 = ManufactureProductAdapterV2(context)
        products.adapter = adapterV2
        load(orderType)
        layout_def.setOnClickListener {
            if (index != 0){
                index = 0
                pageIndex = 1
                text_def.setTypeface(null,Typeface.BOLD)
                label_def.visibility = View.VISIBLE
                text_new.setTypeface(null,Typeface.NORMAL)
                label_new.visibility = View.GONE
                text_sale.setTypeface(null,Typeface.NORMAL)
                label_sale.visibility = View.GONE
                text_price.setTypeface(null,Typeface.NORMAL)
                label_price_top.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                orderType = 0
                adapterV2.removeAll()
                load(orderType)
            }
        }
        layout_new.setOnClickListener {
            if (index != 1){
                index = 1
                pageIndex = 1
                text_def.setTypeface(null,Typeface.NORMAL)
                label_def.visibility = View.GONE
                text_new.setTypeface(null,Typeface.BOLD)
                label_new.visibility = View.VISIBLE
                text_sale.setTypeface(null,Typeface.NORMAL)
                label_sale.visibility = View.GONE
                text_price.setTypeface(null,Typeface.NORMAL)
                label_price_top.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                orderType = 2
                adapterV2.removeAll()
                load(orderType)
            }
        }
        layout_sale.setOnClickListener {
            if (index != 2){
                index = 2
                pageIndex = 1
                text_def.setTypeface(null,Typeface.NORMAL)
                label_def.visibility = View.GONE
                text_new.setTypeface(null,Typeface.NORMAL)
                label_new.visibility = View.GONE
                text_sale.setTypeface(null,Typeface.BOLD)
                label_sale.visibility = View.VISIBLE
                text_price.setTypeface(null,Typeface.NORMAL)
                label_price_top.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                orderType = 1
                adapterV2.removeAll()
                load(orderType)
            }
        }
        layout_price.setOnClickListener {
            index = 3
            text_def.setTypeface(null,Typeface.NORMAL)
            label_def.visibility = View.GONE
            text_new.setTypeface(null,Typeface.NORMAL)
            label_new.visibility = View.GONE
            text_sale.setTypeface(null,Typeface.NORMAL)
            label_sale.visibility = View.GONE
            pageIndex = 1
            text_price.setTypeface(null,Typeface.NORMAL)
            if (istop){
                istop = false
                label_price_top.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_selected))
                label_price_bottom.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                orderType = 3
                adapterV2.removeAll()
                load(orderType)
            } else {
                istop = true
                label_price_top.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_unselected))
                label_price_bottom.setTextColor(requireContext().getColor(R.color.color_amallv3_product_detail_top_selected))
                orderType = 4
                adapterV2.removeAll()
                load(orderType)
            }
        }
        products.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = manager.childCount
                    var totalItemCount = manager.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isloading && lastVisibleItemPosition == totalItemCount - 1) {
                        isloading = true
                        load(orderType)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = manager.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastVisibleItemPosition)
            }
        })
    }

    private var lastVisibleItemPosition = 0

    var isAutoLoadMoreEnabled = true

    var isloading = false

    lateinit var adapterV2: ManufactureProductAdapterV2

    fun load(order : Int){
        ApiManager.getInstance().getApiAMallV3()
            .searchAMallV3(categoryId = null, pageIndex = pageIndex, pageSize = pageSize,
                manufacturerId = manufactureId,
                keyword = null,sortBy = order)
            .enqueue(object : Callback<AMallV3SearchProductBean> {
                override fun onResponse(
                    call: Call<AMallV3SearchProductBean>,
                    response: Response<AMallV3SearchProductBean>
                ) {
                    if (response.body() == null) {
                        return
                    }
                    isloading = false

                    if (response.body()!!.data == null){
                        return
                    }
                    if (response.body()!!.data.productList == null){
                        return
                    }

                    adapterV2.add(response.body()!!.data.productList)
                    this@ManufactureProductFragmentV2.pageIndex++

                    if (response.body()!!.data.productList.size < pageSize){
                        isAutoLoadMoreEnabled = false
                        StringUtils.log("停止了，这个被禁用了")
                    }
                }

                override fun onFailure(call: Call<AMallV3SearchProductBean>, t: Throwable) {

                }

            })
    }

}