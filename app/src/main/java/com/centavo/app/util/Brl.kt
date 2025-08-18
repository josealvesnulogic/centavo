package com.centavo.app.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

object Brl {
    private val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    fun format(value: BigDecimal) = nf.format(value)
}