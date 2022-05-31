package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_up.*
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.databinding.FragmentUpBinding
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiPersonal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.UpVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean

class UpFragment : MyBaseFragment() {

    private val apiPersonalTest: ApiPersonal = ApiManager.getInstance().getApiPersonalTest()

    lateinit var binding : FragmentUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = GridLayoutManager(context,2)
        binding.list.layoutManager = manager
        loadData()
    }

    fun loadData(){
        apiPersonalTest.loadUpList(UserInfoViewModel.getUserId()).enqueue(object :
            Callback<RecommendVideoBean> {
            override fun onResponse(call: Call<RecommendVideoBean>, response: Response<RecommendVideoBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    empty_layout.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    return
                }
                binding.list.adapter =
                    UpVideoAdapter(context, response.body()?.data)
            }

            override fun onFailure(call: Call<RecommendVideoBean>, t: Throwable) {

            }

        })
    }

}