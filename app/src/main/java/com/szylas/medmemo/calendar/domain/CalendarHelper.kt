package com.szylas.medmemo.calendar.domain

import java.util.Calendar

fun generateDates(start: Calendar, count: Int): ArrayList<Calendar> {
    if (count < 2) {
        throw IllegalArgumentException(
            "Number of dates to be generated should be at least 2, but is $count"
        )
    }
    val calendars = arrayListOf(
        start
    )
    for (i in 1..<count) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, i)
        calendars.add(calendar)
    }

    return calendars
}