package com.szylas.medmemo.common.domain.models

import java.io.Serializable
import java.util.Calendar

data class Memo(
    var name: String = "",
    var numberOfDoses: Int = 1,
    var smartMode: Boolean = false,
    var dosageTime: List<Int> = listOf(),
    var startDate: Calendar = Calendar.getInstance(),
    var finishDate: Calendar? = null,
    var gap: Int = 0,
    var notifications: MutableList<MemoNotification> = mutableListOf()
) : Serializable
