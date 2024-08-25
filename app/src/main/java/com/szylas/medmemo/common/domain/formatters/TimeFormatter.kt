package com.szylas.medmemo.common.domain.formatters

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Calendar

fun formatTime(time: Int): String {
    val f: NumberFormat = DecimalFormat("00")
    return "${f.format(time/60)}:${f.format(time%60)}"
}

fun formatTime(time: Calendar): String {
    val f: NumberFormat = DecimalFormat("00")
    return "${f.format(time.get(Calendar.HOUR_OF_DAY))}:${f.format(time.get(Calendar.MINUTE))}"
}