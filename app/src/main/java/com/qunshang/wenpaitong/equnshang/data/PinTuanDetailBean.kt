package com.qunshang.wenpaitong.equnshang.data


data class PinTuanDetailBean(
    val groupInfo: GroupInfo,
    val userGroupInfo: UserGroupInfo,
    val orderId: Int,
    val product: Product,
    val skuList: List<Sku>,
    val specList: List<Spec>
)

data class GroupInfo(
    val cashSuccessLeftPeople: Int,
    val expireTime: String,
    val groupSuccessLeftPeople: Int,
    val isNBackOne: Int,
    val status: Int,
    val userInfo: List<UserInfo>,
    val orderGroupSn: String,
)

data class UserGroupInfo(
    val isOwner: Int,
    val joinGroup: Int
)

data class Product(
    val manufactureHeadImg: String,
    val manufactureName: String,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val skuInfo: String,
    val skuPic: String
)

data class UserInfo(
    val headImage: String,
    val name: String
)

data class Sku(
    val id: Int,
    val image: String,
    val marketPrice: Double,
    val price: Double,
    val stock: Int,
    val value: String,
    val vipGroupPrice: Double,
    val vipPrice: Double
)

data class Spec(
    val list: ArrayList<String>,
    val name: String
)