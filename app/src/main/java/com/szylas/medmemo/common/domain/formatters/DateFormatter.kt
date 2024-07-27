package com.szylas.medmemo.common.domain.formatters

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatDate(calendar: Calendar) : String {
    return SimpleDateFormat(
        "dd-MMM", Locale.getDefault()
    ).format(calendar.time)
}