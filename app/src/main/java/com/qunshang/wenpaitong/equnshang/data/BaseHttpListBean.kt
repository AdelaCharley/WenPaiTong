package com.qunshang.wenpaitong.equnshang.data

/**
 * 网络请求数据
 * create by 何姝霖
 */
data class BaseHttpListBean<T> (
    val code: Int,
    val msg: String,
    val data: List<T>?
)

