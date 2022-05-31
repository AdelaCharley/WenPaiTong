package com.qunshang.wenpaitong.equnshang.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.dialog_spec.*

class SpecDialog(val list : List<com.qunshang.wenpaitong.equnshang.data.ProductBean.DataBean.AttributeList>) : BaseBottomSheetDialog() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_spec,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        list_spec.layoutManager = linearLayoutManager
        list_spec.adapter = com.qunshang.wenpaitong.equnshang.adapter.SpecAdapter(context, list)
        confirm.setOnClickListener { dismiss() }
    }

    override val height: Int
        get() = dp2px(requireContext(),600.0f)

}