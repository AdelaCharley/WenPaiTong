package com.qunshang.wenpaitong.equnshang.data

import androidx.lifecycle.ViewModel

object VendorModel : ViewModel() {

    private var vendorId = 0

    fun getVendorId() : Int {
        return vendorId
    }

    fun setVendorId(vendorId : Int){
        this.vendorId = vendorId
    }

}