package com.szylas.medmemo.common.domain.models

import java.text.DecimalFormat
import java.text.NumberFormat

data class TreatmentModel(
    val title: String,
    val id: Long,
    val time: Int
) {
    fun timeString(): String {
        val f: NumberFormat = DecimalFormat("00")
        return "${f.format(time/60)}:${f.format(time%60)}"
    }
}
