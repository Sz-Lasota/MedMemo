package com.szylas.medmemo.calendar.domain

import java.util.Calendar

fun generateDates(start: Calendar, count: Int): ArrayList<Calendar> {
    val calendars = arrayListOf(
        start
    )
    for (i in 1..<count){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, i)
        calendars.add(calendar)
    }

    return calendars
}