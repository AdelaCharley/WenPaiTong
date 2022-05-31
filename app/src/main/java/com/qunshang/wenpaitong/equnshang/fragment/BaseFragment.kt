package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife

/**
 * create by libo
 * create on 2020-05-19
 * description
 */
abstract class BaseFragment : MyBaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(setLayoutId(), container, false)
        ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("zhangjuniii","onViewCreated被调用")
        init()
    }

    protected abstract fun setLayoutId(): Int

    open fun init(){

    }
}