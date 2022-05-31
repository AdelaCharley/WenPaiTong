package com.qunshang.wenpaitong.equnshang.data

data class UserFocusCollectNum(
    val focusAndCollect: FocusAndCollect,
    val activity: List<Activities>,
    val tools: List<BottomIcon>
    )

data class FocusAndCollect(
    val collectNum: Int,
    val focusNum: Int
)

data class Activities(
    val title: String,
    val subTitle: String,
    val image: String
)

data class BottomIcon(
    val title: String,
    val image: String,
    val key: String
)