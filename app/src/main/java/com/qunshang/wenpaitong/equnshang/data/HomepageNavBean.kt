package com.qunshang.wenpaitong.equnshang.adapter

/**
 * "我的主页"导航栏数据
 * create by 何姝霖
 */
data class HomepageNavBean(
    val behavior: List<Behavior>,
    val isPartner: Boolean
)

data class Behavior(
    val behaviorTxt: String,
    val choose: Boolean,
    val number: Int
)