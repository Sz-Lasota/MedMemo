package com.szylas.medmemo.common.domain.models

import java.io.Serializable
import java.util.Calendar

data class MemoNotification(
    val date: Calendar = Calendar.getInstance(),
    val name: String = "",
    var intakeTime: Calendar? = null,
    val notificationId: Int = 0
) : Serializable