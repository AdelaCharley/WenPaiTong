package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_concern.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.FragmentConcernBinding
import com.qunshang.wenpaitong.equnshang.adapter.ConcernAdapter
import com.qunshang.wenpaitong.equnshang.data.NewConcernBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class ConcernFragment : MyBaseFragment() {

    //private val apiPersonalTest: ApiPersonal = ApiManager.getInstance().getApiPersonalTest()

    lateinit var binding : FragmentConcernBinding

    var pageIndex = 1

    val pageSize = 20

    var isFansAAutoLoadMoreEnabled = true

    var isFansAloading = false

    private var lastFansAVisibleItemPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConcernBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.list.layoutManager = manager
        /*doneworder.setOnClickListener {
            BottomMenu.show(
                (context as AppCompatActivity?)!!, arrayOf("近7日访问频次", "关注时间从新至旧","关注时间从旧至新","粉丝量从高至低")
            ) { text: String?, index: Int ->

            }.title = "排序选择"
        }*/
        val managerFansA = LinearLayoutManager(requireContext())
        managerFansA.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = managerFansA
        list.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFansAAutoLoadMoreEnabled){
                    return
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var visibleItemCount = managerFansA.childCount
                    var totalItemCount = managerFansA.itemCount
                    StringUtils.log("当前的totalItemCOunt" + totalItemCount)
                    if (!isFansAloading && lastFansAVisibleItemPosition == totalItemCount - 1) {
                        isFansAloading = true
                        loadData()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastFansAVisibleItemPosition = managerFansA.findLastVisibleItemPosition()
                StringUtils.log("最后可见" + lastFansAVisibleItemPosition)
            }
        })
        adapter = ConcernAdapter(requireContext())
        list.adapter = adapter
        loadData()
    }

    fun loadData(){
        /*apiPersonalTest.loadAttentionList(UserInfoViewModel.getUserId()).enqueue(object : Callback<ConcernData>{
            override fun onResponse(call: Call<ConcernData>, response: Response<ConcernData>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    empty_layout.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    return
                }
                binding.list.adapter =
                    ConcernAdapter(context, response.body()?.data)
            }

            override fun onFailure(call: Call<ConcernData>, t: Throwable) {

            }

        })*/
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().loadNewConcernList(userId = UserInfoViewModel.getUserId(),pageIndex = pageIndex,pageSize = pageSize).enqueue(object : Callback<NewConcernBean>{
            override fun onResponse(
                call: Call<NewConcernBean>,
                response: Response<NewConcernBean>
            ) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.code != 200){
                    return
                }
                isFansAloading = false
                adapter.add(response.body()!!.data)

                if (response.body()!!.data?.size == 0 && pageIndex == 1){
                    list.visibility = View.GONE
                    empty_layout.visibility = View.VISIBLE
                }

                this@ConcernFragment.pageIndex++
                if (response.body()!!.data.size < pageSize){
                    isFansAAutoLoadMoreEnabled = false
                    StringUtils.log("停止了，这个被禁用了")
                }

            }

            override fun onFailure(call: Call<NewConcernBean>, t: Throwable) {

            }

        })
    }

    lateinit var adapter: ConcernAdapter

}