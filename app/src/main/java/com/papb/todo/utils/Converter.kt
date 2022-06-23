package com.papb.todo.utils

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Converter {
    fun numberFormat(number: Int): String {
        return DecimalFormat("###,###,###,###").format(number)
    }

    fun formatRupiah(number: Int): String {
        return "Rp. " + DecimalFormat("###,###,###,###").format(number)
    }

    fun formatRupiah(number: Float): String {
        return DecimalFormat("###,###,###,###").format(number)
    }

    fun dateTimeFormat(date: String): String {
        var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val newDate: Date? = format.parse(date)
        format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return format.format(newDate)
    }

    fun dateTimetoDateFormat(date: String): String {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var newDate: Date? = format.parse(date)
        format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return format.format(newDate)
    }

    fun dateFormat(date: String): String {
        var format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var newDate: Date? = format.parse(date)
        format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return format.format(newDate)
    }

    fun dateTimetoTimeFormat(date: String): String {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var newDate: Date? = format.parse(date)
        format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(newDate)
    }

    @Throws(ParseException::class)
    fun timeFormat(date: String): String {
        var format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        var newDate: Date? = format.parse(date)
        format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(newDate)
    }

    fun decimalFormat(number: Int): String {
        val numberFormat = DecimalFormat("00")
        return numberFormat.format(number.toLong())
    }
}