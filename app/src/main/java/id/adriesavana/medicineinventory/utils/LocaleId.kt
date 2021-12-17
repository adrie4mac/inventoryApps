@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package id.adriesavana.medicineinventory.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.Locale

object LocaleId {
    const val FORMAT_DATE_TIME_YMDHMS = "yyyy-MM-dd HH:mm:ss"
    val LOCALE_ID = Locale("in", "ID")
    internal val FORMAT_MONEY = NumberFormat.getCurrencyInstance(LOCALE_ID).apply {
        currency = Currency.getInstance(LOCALE_ID)
        minimumFractionDigits = 0
        maximumFractionDigits = 2
        isGroupingUsed = true
        if (this is DecimalFormat){
            groupingSize = 3
            isDecimalSeparatorAlwaysShown = false
        }
    }

    fun formatCurrencyFromDouble(amount: String): String {
        return formatCurrency(amount.toDoubleOrNull() ?: 0.0)
    }

    fun formatCurrencyFromLong(amount: String): String {
        return formatCurrency(amount.toLongOrNull() ?: 0L)
    }

    fun formatCurrency(amount: Double): String {
        return FORMAT_MONEY.format(amount)
    }

    fun formatCurrency(amount: Long): String {
        return FORMAT_MONEY.format(amount)
    }
}