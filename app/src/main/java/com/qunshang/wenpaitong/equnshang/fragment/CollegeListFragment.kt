package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialog.v3.WaitDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_college_list.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.FragmentCollegeListBinding
import com.qunshang.wenpaitong.equnshang.activity.BaseActivity
import com.qunshang.wenpaitong.equnshang.adapter.CollegeNewListAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.CollegeNewItemBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.util.concurrent.TimeUnit

/**
 * 商学院-关注页&推荐页
 * create by 何姝霖
 */
class CollegeListFragment(var agencyId: Int? = null,var isRefreshTop : Boolean = false) : Fragment() {

    private val TAG = "shu-CollegeListFragment"
    private lateinit var binding: FragmentCollegeListBinding
    private val adapter = CollegeNewListAdapter()
    private var pageIndex = 1
    private var pageSize = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollegeListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as BaseActivity
        //activity.setSystemBarColor(R.color.black);
        //activity.setSystemBarColor(R.color.black);
        activity.changeToGreyButTranslucent()
        initLayout()
        if (isRefreshTop){
            root.setPadding(0,0,0,0)
            val marginrecyc = layout.layoutParams as ViewGroup.MarginLayoutParams
            marginrecyc.setMargins(marginrecyc.leftMargin,0,marginrecyc.rightMargin,marginrecyc.bottomMargin)
            top?.visibility = View.GONE
        } else {
            top?.visibility = View.VISIBLE
            toolbar_back?.visibility = View.INVISIBLE
            toolbar_title?.text = "商学院"
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (isHidden) {
        } else {
            val activity = requireActivity() as BaseActivity
            activity.changeToGreyButTranslucent()
        }
    }

    var isFansAAutoLoadMoreEnabled = true

    private var isLoading = false
    private var lastVisibleItemPosition = 0
    private fun initLayout() {
        val recycleList = binding.recycleList
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleList.layoutManager = layoutManager
        recycleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFansAAutoLoadMoreEnabled){
                    return
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading && lastVisibleItemPosition == layoutManager.itemCount - 1) {
                        isLoading = true
                        loadData()
                    }
                }

            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
        })
        recycleList.adapter = adapter
        layout.setOnRefreshListener {
            //DialogUtil.toast(requireContext(),"附近的空间发看")
            layout.isRefreshing = false
            adapter.clear()
            pageIndex = 1
            isFansAAutoLoadMoreEnabled = true
            loadData(true)
        }
        loadData()
    }

    private fun loadData(isShowText : Boolean = false) {
        WaitDialog.show(requireActivity() as AppCompatActivity,"正在加载")
        ApiManager.getInstance()
            .getApiWenBanTong_HeShuLin()
            .loadCollegeNewList(agencyId, pageIndex, pageSize)
            .enqueue(object : Callback<BaseHttpListBean<CollegeNewItemBean>> {
                override fun onResponse(
                    call: Call<BaseHttpListBean<CollegeNewItemBean>>,
                    response: Response<BaseHttpListBean<CollegeNewItemBean>>
                ) {
                    isLoading = false
                    WaitDialog.dismiss()
                    Log.d(TAG, "onResponse-loadCollegeNewList: $response")
                    if (response.isSuccessful) {
                        adapter.add(response.body()?.data)
                        if (isShowText){
                            showText()
                        }
                    }
                    pageIndex++
                    if (response.body()!!.data?.size!! < pageSize){
                        isFansAAutoLoadMoreEnabled = false
                        StringUtils.log("停止了，这个被禁用了")
                    }
                }

                override fun onFailure(
                    call: Call<BaseHttpListBean<CollegeNewItemBean>>,
                    t: Throwable
                ) {
                    WaitDialog.dismiss()
                }

            })
    }

    fun showText(){
        showtext.visibility = View.VISIBLE
        /*Thread.sleep(1000)
        showtext.visibility = View.GONE*/
        Observable
            .timer(1,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<Long>{
                override fun accept(t: Long) {
                    showtext.visibility = View.GONE
                }
            })
    }

}