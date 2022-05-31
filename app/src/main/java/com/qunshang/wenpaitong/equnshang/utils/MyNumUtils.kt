package com.qunshang.wenpaitong.equnshang.utils

import java.math.BigDecimal

class MyNumUtils {

    companion object {
        fun remain(num : Double) : Double{
            val bg = BigDecimal(num);
            val d3 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
            return d3
        }

        fun makeDoubleToString(num : Double) : String{
            val bg = BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP)
            return bg.toPlainString()
        }

    }

}