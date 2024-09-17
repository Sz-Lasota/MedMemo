package com.szylas.medmemo.common.datastore.models

import java.io.Serializable
import java.util.Calendar

data class MemoEntity(
    var name: String = "",
    var numberOfDoses: Int = 0,
    var smartMode: Boolean = false,
    var dosageTime: List<Int> = listOf(),
    var startDate: Long = Calendar.getInstance().timeInMillis,
    var finishDate: Long? = null,
    var gap: Int = 0,
    var notifications: MutableList<MemoNotificationEntity> = mutableListOf()
) : Serializable
