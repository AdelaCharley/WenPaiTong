package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.fragment_manufacture_video.*
import com.qunshang.wenpaitong.equnshang.adapter.ManufactureVideoAdapter
import com.qunshang.wenpaitong.equnshang.data.QiShiTongVideoBean

class ManufactureVideoFragment(val data : ArrayList<QiShiTongVideoBean.DataBean>?) : MyBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manufacture_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (data?.size == 0){
            list.visibility = View.GONE
            empty_layout.visibility = View.VISIBLE
            return
        }
        val manager = GridLayoutManager(context,2)
        list.layoutManager = manager
        list.adapter = ManufactureVideoAdapter(context, data)
    }

}