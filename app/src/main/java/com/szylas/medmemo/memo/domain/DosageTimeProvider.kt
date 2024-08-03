package com.szylas.medmemo.memo.domain

import android.util.Log
import com.szylas.medmemo.common.domain.models.Memo

fun Memo.provideTimes(): List<Int> {
    return MutableList(this.numberOfDoses) {
        it * this.gap + 8 * 60
    }
}