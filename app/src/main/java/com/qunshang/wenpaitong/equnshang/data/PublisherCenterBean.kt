package com.qunshang.wenpaitong.equnshang.data

data class PublisherCenterBean(
    val agencyId : Int,
    val agencyAvatar : String,
    val agencyName : String,
    val agencyDescription : String,
    val fanNum : Int,
    val newsNum : Int,
    val totalUpNum : Int,
    val isFocus : Int
)