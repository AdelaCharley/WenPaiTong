package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a_mall_v3_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.AMallV3SearchActivity
import com.qunshang.wenpaitong.equnshang.adapter.GuessProductAdapterNewYear
import com.qunshang.wenpaitong.equnshang.data.AMallV3MainBean
import com.qunshang.wenpaitong.equnshang.data.AMallV3SearchProductBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class AMallV3MainFragmentNewYear : MyBaseFragment() {

    var keyword: String? = null

    var categoryId: Int? = null

    var manufacturerId: Int? = null

    var pageIndex = 1

    val pageSize = 20

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a_mall_v3_main_new_year, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_layoout.setOnClickListener {
            val intent = Intent(context, AMallV3SearchActivity::class.java)
            startActivity(intent)
        }
        guessProductAdapter = GuessProductAdapterNewYear(requireContext())
        guesslike.isNestedScrollingEnabled = false
        guesslike.setHasFixedSize(false)
        guesslike.isFocusable = false
        guesslike.adapter = guessProductAdapter

        val manager = GridLayoutManager(requireContext(),2)
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
                        loadGuessAdapter()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = manager.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastVisibleItemPosition)
            }
        })
        toolbar_back.setOnClickListener { requireActivity().finish() }
        toolbar_title.text = ""
        if (toolbar_back.parent is View){
            var view = toolbar_back.parent as View
            StringUtils.log("parent的idshig" + view.id)
            view.background = null
        }
        //toolbar_title?.parent.ba = resources.getDrawable(R.color.transparent)
        ApiManager.getInstance().getApiAMallV3().loadAMallV3MainBean().enqueue(object : Callback<AMallV3MainBean> {
            override fun onResponse(
                    call: Call<AMallV3MainBean>,
                    response: Response<AMallV3MainBean>
            ) {
                if (response.body() == null){
                    return
                }
                guessProductAdapter.addHeader(response.body()!!)
            }

            override fun onFailure(call: Call<AMallV3MainBean>, t: Throwable) {

            }

        })


        loadGuessAdapter()
    }

    private var lastVisibleItemPosition = 0

    var isAutoLoadMoreEnabled = true

    var isloading = false

    lateinit var guessProductAdapter: GuessProductAdapterNewYear

    fun loadGuessAdapter(){
        StringUtils.log("啊啊啊我当前hi" + pageIndex)
        ApiManager.getInstance().getApiAMallV3()
                .searchAMallV3(categoryId = null, pageIndex = pageIndex, pageSize = pageSize,
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
                        if (response.body()!!.data.productList == null){
                            return
                        }
                        guessProductAdapter.add(response.body()!!.data.productList)
                        this@AMallV3MainFragmentNewYear.pageIndex++
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