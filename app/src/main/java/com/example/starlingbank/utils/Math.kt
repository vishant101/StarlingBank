package com.example.starlingbank.utils

import java.math.BigDecimal

fun round(d: Float, decimalPlace: Int): BigDecimal {
    var bd = BigDecimal(d.toString())
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
    return bd
}