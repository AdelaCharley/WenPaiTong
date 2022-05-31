package com.qunshang.wenpaitong.equnshang.data

/**
 *
 * create by 何姝霖
 */
data class AfterSaleDetailBean(
    val errMsg: String,
    val manufactureAddressInfo: ExchangeBean.DataBean.VendorAddress,
    val orderAfterSaleStatus: Int,
    val product: AfterSaleProduct,
    val refundAmount: Double,
    val afterSaleInfo: AfterSaleInfo,
    val userAddressInfo: UserAddressInfo,
    val userSendInfo: UserSendInfo,
    val statusText:String
)

data class AfterSaleProduct(
    val manufactureHeadImg: String,
    val manufactureId: Int,
    val productId: Int,
    val productName: String,
    val productQuantity: Int,
    val productSinglePrice: Double,
    val skuInfo: String,
    val skuPic: String,
    val vendorName: String
)

data class AfterSaleInfo(
    val afterSaleSn: String,
    val createTime: String,
    val orderAfterSaleType: Int,
    val refundExplain: String,
    val refundPic: String,
    val refundReason: String
)

data class UserAddressInfo(
    val userAddressLocation: String,
    val userAddressName: String,
    val userAddressPhone: String
)

data class UserSendInfo(
    val sendFunction: String,
    val userExpressCompanyName: String,
    val userExpressSn: String?
)