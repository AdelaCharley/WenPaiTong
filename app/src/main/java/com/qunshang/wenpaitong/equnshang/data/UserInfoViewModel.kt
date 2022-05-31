package com.qunshang.wenpaitong.equnshang.data

import androidx.lifecycle.ViewModel

/**
 * 单例模式，存储当前登录用户的信息
 * create by 何姝霖
 */
object UserInfoViewModel : ViewModel() {

    const val MALE = 0
    const val FEMALE = 1


    private var userInfo: UserMsgBean.UserInfoBean? = null
    private var userId = ""

    fun getUserId(): String {
        return userId
    }
    fun setUserId(userId: String){
        this.userId = userId
    }

    fun getUserInfo(): UserMsgBean.UserInfoBean? {
        return userInfo
    }
    fun setUserInfo(userInfo: UserMsgBean.UserInfoBean?){
        UserInfoViewModel.userInfo = userInfo
    }

}