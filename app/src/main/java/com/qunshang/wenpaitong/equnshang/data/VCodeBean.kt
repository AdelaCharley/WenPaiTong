package com.qunshang.wenpaitong.equnshang.data

/**
 * 登陆注册页面，获取的验证码实体
 * create by 何姝霖
 */
data class VCodeBean(
    var RequestId: String,
    val Message: String,
    val BizId: String,//不知道是啥，但登陆注册验证时，该变量是网络请求参数之一
    val Code: String

)