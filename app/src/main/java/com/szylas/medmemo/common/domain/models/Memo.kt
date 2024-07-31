package com.szylas.medmemo.common.domain.models

import java.io.Serializable
import java.util.Calendar

data class Memo(
    var name: String = "",
    var description: String = "",
    var smartMode: Boolean = false,
    var dosageTime: List<Int> = listOf(),
    var startDate: Calendar = Calendar.getInstance(),
    var finishDate: Calendar? = null
): Serializable
