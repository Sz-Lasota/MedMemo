package com.szylas.medmemo.common.domain.formatters

import java.text.DecimalFormat
import java.text.NumberFormat

fun timeString(time: Int): String {
    val f: NumberFormat = DecimalFormat("00")
    return "${f.format(time/60)}:${f.format(time%60)}"
}