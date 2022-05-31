package com.qunshang.wenpaitong.equnshang.activity

import com.qunshang.wenpaitong.equnshang.data.Agency
import com.qunshang.wenpaitong.equnshang.data.News

data class MyCollectionItemBean(
    val agency: Agency,
    val news: News,
    val collectId: Int
)