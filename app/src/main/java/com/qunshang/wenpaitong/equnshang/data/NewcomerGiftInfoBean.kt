package com.qunshang.wenpaitong.equnshang.data

data class NewcomerGiftInfoBean(
    var endTime: String,
    val obtainInfo: Any,
    val product: NewerProduct?,
    val status: Int
)
data class NewerProduct (
    val mainUrl : String,
    val title : String,
    val subTitle : String,
    val price : String
    )