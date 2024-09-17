package com.szylas.medmemo.common.datastore.models

import java.io.Serializable
import java.util.Calendar


data class MemoNotificationEntity(
    val date: Long = Calendar.getInstance().timeInMillis,
    val name: String = "",
    var intakeTime: Long? = null,
    val baseDosageTime: Int = 0,
    val notificationId: Int = 0
) : Serializable
