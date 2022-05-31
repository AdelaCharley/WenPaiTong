package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import kotlinx.android.synthetic.main.fragment_manufacture_product.*
import com.qunshang.wenpaitong.equnshang.adapter.ManufactureProductAdapter
import com.qunshang.wenpaitong.equnshang.data.StoreDataBean

class ManufactureProductFragment(val data : List<StoreDataBean.DataBean.Product>,val type : String) : MyBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manufacture_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.VERTICAL
        products.layoutManager = manager
        products.adapter = ManufactureProductAdapter(context, data,type)
    }

}