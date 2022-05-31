package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_collection.*
import kotlinx.android.synthetic.main.activity_my_collection.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.ActivityMyCollectionBinding
import com.qunshang.wenpaitong.databinding.LayoutToolbarBinding
import com.qunshang.wenpaitong.equnshang.adapter.MyCollectionListAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class MyCollectionActivity: BaseActivity() {
    private val TAG = "shu-MyCollection"
    private lateinit var binding: ActivityMyCollectionBinding
    private var userId = -1
    private lateinit var toolbar: LayoutToolbarBinding
    private lateinit var list: RecyclerView
    private lateinit var layoutBottom: LinearLayoutCompat
    private lateinit var adapter: MyCollectionListAdapter

    companion object {
        private const val INDEX_EDIT = 0
        //        private const val INDEX_CANCEL = 1
        private val TEXT_EDIT_CANCEL = arrayOf("编辑", "取消")  // toolbar右侧文字
        private const val HINT_CLEAR_ALL = "一键清空？"
        private const val HINT_NO_EDITABLE = "暂无可编辑内容"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MyCollectionListAdapter(this)
        userId = UserHelper.getCurrentLoginUser(this).toInt()
        toolbar = binding.toolbar
        list = binding.recycleList
        layoutBottom = binding.layoutBottom
        EventBus.getDefault().register(this)
        initLayout()
    }

    private var index = INDEX_EDIT

    private fun initLayout() {
        loadData()
        initToolbar()
        initLayoutList()
        initLayoutBottom()
    }

    private fun initToolbar() {
        toolbar.toolbarTitle.text = "收藏"
        toolbar.toolbarRight.text = TEXT_EDIT_CANCEL[index]
        toolbar.toolbarRight.setOnClickListener { toEditOrCancel() }
        toolbar.toolbarBack.setOnClickListener { finish() }
    }

    private var isLoadMoreEnable = true
    private var isLoading = false
    private var lastVisibleItemPosition = 0
    private fun initLayoutList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = layoutManager
        list.adapter = adapter
        list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isLoadMoreEnable) return
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading
                    && lastVisibleItemPosition == layoutManager.itemCount - 1) {
                    isLoading = true
                    loadData()
                }
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
        })
    }

    private fun initLayoutBottom() {
        layoutBottom.tv_clear.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(HINT_CLEAR_ALL)
                .setPositiveButton("确定") { _, _ ->
                    toDeleteCollection(1, null)
                    toEditOrCancel()
                }
                .setNegativeButton("取消") { _, _ ->
                    toEditOrCancel()
                }
                .show()
        }
        layoutBottom.tv_delete.setOnClickListener {
            toDeleteCollection(0, adapter.getSelectedId())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setTvDelete(eventMsg: String) {
        Log.d(TAG, "setTvDelete: ")
        layoutBottom.tv_delete.text = eventMsg
    }

    private fun toEditOrCancel() {
        adapter.toSelect()
        if (index == INDEX_EDIT && list.adapter?.itemCount == 0) {
            DialogUtil.toast(this, HINT_NO_EDITABLE)
        } else {
            index = index xor 1
            toolbar.toolbarRight.text = TEXT_EDIT_CANCEL[index]
            layoutBottom.visibility = if (layoutBottom.isVisible) View.GONE else View.VISIBLE
        }

    }

    private fun toDeleteCollection(isAll: Int?, collectionsId: IntArray?) {
        val collection = DeleteCollectionBean(userId, isAll, collectionsId)
        val jsonData = Gson().toJson(collection).toString()
        val mediaType: MediaType = "application/json;charset=utf-8".toMediaTypeOrNull()!!
        val data = jsonData.toRequestBody(mediaType)
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .deleteCollections(data)
            .enqueue(object: Callback<BaseHttpBean<String>> {
                override fun onResponse(
                    call: Call<BaseHttpBean<String>>,
                    response: Response<BaseHttpBean<String>>
                ) {
                    Log.d(TAG, "toDeleteCollection: ${response.body()}")
                    if (response.isSuccessful) {
                        adapter.remove(isAll)
                        toEditOrCancel()
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                }
            })
    }

    private var pageIndex = 1
    private var pageSize = 10
    private fun loadData() {
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .loadMyCollectionList(userId, pageIndex, pageSize)
            .enqueue(object: Callback<BaseHttpListBean<MyCollectionItemBean>> {
                override fun onResponse(
                    call: Call<BaseHttpListBean<MyCollectionItemBean>>,
                    response: Response<BaseHttpListBean<MyCollectionItemBean>>
                ) {
                    StringUtils.log("loadData: $response")
                    if (response.isSuccessful) {
                        adapter.add(response.body()?.data)
                        if (response.body()!!.data?.size == 0 && pageIndex == 1){
                            list.visibility = View.INVISIBLE
                            empty_layout.visibility = View.VISIBLE
                        }
                        if (response.body()!!.data?.size == 0){
                            StringUtils.log("啊0")
                        } else {
                            StringUtils.log("减肥肯德基发干的快")
                        }
                        pageIndex++
                        isLoadMoreEnable = response.body()!!.data?.size!! >= pageSize
                    }
                }

                override fun onFailure(
                    call: Call<BaseHttpListBean<MyCollectionItemBean>>,
                    t: Throwable
                ) {
                    StringUtils.log("onFailure: ")
                }

            })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    inner class DeleteCollectionBean(
        val userId: Int,
        val isAll: Int? = null,
        val collectionsId: IntArray? = null)
}

