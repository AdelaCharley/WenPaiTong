package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_first_category_page.guesslike
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.GuessProductAdapterFirstPage
import com.qunshang.wenpaitong.equnshang.data.AMallV3FirstCategoryPageBean
import com.qunshang.wenpaitong.equnshang.data.AMallV3SearchProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class FirstCategoryPageActivity : BaseActivity() {

    var categoryId = -9

    var keyword: String? = null

    var manufacturerId: Int? = null

    var pageIndex = 1

    val pageSize = 20

    lateinit var guessProductAdapter: GuessProductAdapterFirstPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_category_page)
        categoryId = intent.getIntExtra("categoryId",-9)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText(intent.getStringExtra("name"))
        if (categoryId == -9){
            DialogUtil.toast(this,"出错了")
            return
        }
        guessProductAdapter = GuessProductAdapterFirstPage(this)
        guesslike.isNestedScrollingEnabled = false
        guesslike.setHasFixedSize(false)
        guesslike.isFocusable = false
        guesslike.adapter = guessProductAdapter
        val manager = GridLayoutManager(this,2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 0){
                    return 2
                } else {
                    return 1
                }
            }
        }
        guesslike.layoutManager = manager
        guesslike.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                        loadProducts()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = manager.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastVisibleItemPosition)
            }
        })
        ApiManager.getInstance().getApiAMallV3().loadFirstPageCategory(categoryId).enqueue(object :
            Callback<AMallV3FirstCategoryPageBean> {
            override fun onResponse(
                call: Call<AMallV3FirstCategoryPageBean>,
                response: Response<AMallV3FirstCategoryPageBean>
            ) {
                if (response.body() == null){
                    return
                }
                guessProductAdapter.addHeader(response.body()!!)
            }

            override fun onFailure(call: Call<AMallV3FirstCategoryPageBean>, t: Throwable) {

            }

        })

        loadProducts()

    }

    fun loadProducts(){
        ApiManager.getInstance().getApiAMallV3()
            .searchAMallV3(categoryId = categoryId, pageIndex = pageIndex, pageSize = pageSize,
                manufacturerId = manufacturerId,
                keyword = keyword)
            .enqueue(object : Callback<AMallV3SearchProductBean> {
                override fun onResponse(
                    call: Call<AMallV3SearchProductBean>,
                    response: Response<AMallV3SearchProductBean>
                ) {
                    if (response.body() == null) {
                        return
                    }
                    isloading = false
                    guessProductAdapter.add(response.body()!!.data.productList)
                    this@FirstCategoryPageActivity.pageIndex++

                    if (response.body()!!.data.productList == null){
                        isAutoLoadMoreEnabled = false
                        return
                    }

                    if (response.body()!!.data.productList.size < pageSize){
                        isAutoLoadMoreEnabled = false
                    }

                }

                override fun onFailure(call: Call<AMallV3SearchProductBean>, t: Throwable) {

                }

            })
    }

    private var lastVisibleItemPosition = 0

    var isAutoLoadMoreEnabled = true

    var isloading = false

}