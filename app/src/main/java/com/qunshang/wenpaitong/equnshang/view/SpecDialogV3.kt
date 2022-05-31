package com.qunshang.wenpaitong.equnshang.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.core.BottomPopupView
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.adapter.SpecAdapterV2
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2

class SpecDialogV3(context: Context, val list : List<ProductBeanV2.DataBean.AttributeList>) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        val linearLayoutManager = LinearLayoutManager(context)
        val list_spec : RecyclerView = findViewById(R.id.list_spec)
        val confirm = findViewById<View>(R.id.confirm)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        list_spec.layoutManager = linearLayoutManager
        list_spec.adapter = SpecAdapterV2(context, list)
        confirm.setOnClickListener {
            dismiss()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_specv3
    }

}