package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_my_commission.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.databinding.FragmentMyCommissionBinding
import com.qunshang.wenpaitong.equnshang.adapter.MyCommissionHeadAdapter
import com.qunshang.wenpaitong.equnshang.data.BaseHttpListBean
import com.qunshang.wenpaitong.equnshang.data.MyCommissionBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

/**
 * 个人中心 -> 佣金管理
 * create by 何姝霖
 */
class MyCommissionFragment: Fragment() {
    private val TAG = "shu-MyCommissionF"

    private lateinit var binding: FragmentMyCommissionBinding
    private var userId = -1
    private lateinit var headAdapter: MyCommissionHeadAdapter
    private lateinit var list: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyCommissionBinding.inflate(layoutInflater, container, false)
        userId = UserHelper.getCurrentLoginUser(requireContext()).toInt()
        list = binding.list
        initList()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (isDataLoaded){
            return
        }
        getCommissionList(requireContext())
        isDataLoaded = true
    }

    var isDataLoaded = false

    private fun initList() {
        val layoutManager = LinearLayoutManager(requireContext())
        list.layoutManager = layoutManager
    }

    private fun getCommissionList(context: Context) {
        ApiManager.getInstance().getApiWenBanTong_HeShuLin()
            .getCommissionList(userId)
            .enqueue(object: Callback<BaseHttpListBean<MyCommissionBean>>{
                override fun onResponse(call: Call<BaseHttpListBean<MyCommissionBean>>,
                                        response: Response<BaseHttpListBean<MyCommissionBean>>) {
                    Log.d(TAG, "onResponse: $response")
                    var data : List<MyCommissionBean> ? = listOf<MyCommissionBean>()
                    if (response.isSuccessful) {
                        if (response.body()?.code == 200){
                            data = response.body()!!.data
                            if (data == null){
                                return
                            }
                            headAdapter = MyCommissionHeadAdapter(context, data)
                            list.adapter = headAdapter
                            if (data.size == 0){
                                layout_weiong.visibility = View.VISIBLE
                            }
                        } else if (response.body()?.code == 400){
                            list.visibility = View.GONE
                            nopermission_layout.visibility = View.VISIBLE
                            val text: CharSequence = label.getText()
                            val builder = SpannableStringBuilder(text)
                            val click: ClickableSpan = object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    //getContext()!!.startActivity(intent)
                                }

                                @RequiresApi(api = Build.VERSION_CODES.M)
                                override fun updateDrawState(ds: TextPaint) {
                                    super.updateDrawState(ds)
                                    ds.isUnderlineText = false
                                    ds.color = requireActivity().getColor(R.color.text_no_permiosson)
                                }
                            }
                            builder.setSpan(click, 3, 13, 0)
                            label.setMovementMethod(LinkMovementMethod.getInstance())
                            label.setText(builder)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseHttpListBean<MyCommissionBean>>,
                                       t: Throwable) { }
            })
    }


}