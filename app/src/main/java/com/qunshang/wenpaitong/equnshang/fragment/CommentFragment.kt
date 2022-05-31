package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_comment.*
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.model.ApiPersonal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.databinding.FragmentCommentBinding
import com.qunshang.wenpaitong.equnshang.adapter.MyCommentAdapter
import com.qunshang.wenpaitong.equnshang.data.MyCommentBean

class CommentFragment : MyBaseFragment() {

    private val apiPersonalTest: ApiPersonal = ApiManager.getInstance().getApiPersonalTest()

    lateinit var binding : FragmentCommentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.list.layoutManager = manager
        loadData()
    }

    fun loadData(){
        apiPersonalTest.loadCommentList(
            //"13515"
            UserInfoViewModel.getUserId()
        ).enqueue(object :
            Callback<MyCommentBean> {
            override fun onResponse(call: Call<MyCommentBean>, response: Response<MyCommentBean>) {
                if (response.body() == null){
                    return
                }
                if (response.body()!!.data.size == 0){
                    empty_layout.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    return
                }
                binding.list.adapter =
                    MyCommentAdapter(context, response.body()?.data)
            }

            override fun onFailure(call: Call<MyCommentBean>, t: Throwable) {

            }

        })
    }
}