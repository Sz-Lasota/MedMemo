package com.szylas.medmemo.memo.domain.extensions

import androidx.compose.ui.graphics.Color
import com.szylas.medmemo.memo.datastore.models.PillCount

fun PillCount.color(): Color {
    val ratio = count / maxAmount.toDouble()
    return if (ratio > 0.45) {
        Color(0xFF2F842E)
    } else if (ratio > 0.25) {
        Color(0xFFC89222)
    } else {
        Color(0xFFB13434)
    }
}

fun PillCount.name(): String = id.split("_")[0]