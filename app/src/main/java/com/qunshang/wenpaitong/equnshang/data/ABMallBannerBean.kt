package com.qunshang.wenpaitong.equnshang.data

class ABMallBannerBean {
    var code = 0
    var msg: String? = null
    var data: List<DataBean>? = null

    class DataBean {
        var bannerUrl: String? = null
        var productId = 0
    }
}