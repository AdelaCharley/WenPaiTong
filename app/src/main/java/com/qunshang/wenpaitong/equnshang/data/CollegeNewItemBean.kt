package com.qunshang.wenpaitong.equnshang.data

data class CollegeNewItemBean(
    val agency: Agency,
    val news: News
)
data class Agency(
    val agencyAvatar: String,
    val agencyId: Int,
    val agencyName: String
)

data class News(
    val browserNum: Int,
    val commentNum: Int,
    val createTime: String,
    val newsId: Int,
    val newsPoster: List<String>,
    val title: String,
    val upNum: Int,
    val subTitle : String
)