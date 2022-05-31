package com.qunshang.wenpaitong.equnshang.data

data class CultureProductBean(
    val productId: Int,
    val productName: String,
    val productPoster: String,
    val productPrice: String,
    val startTime: String,
    val endTime: String,
    val discount: Discount?,
    val tag: String,
    val productInitStock : Int,
    val status: String,
    val companyAvatar : String,
    val companyName : String,
    val evaluationPrice : String
)

data class Discount(
    val productDiscountPrice: String,
    val discountStr: String,
    val productDiscountPriceStr: String
)