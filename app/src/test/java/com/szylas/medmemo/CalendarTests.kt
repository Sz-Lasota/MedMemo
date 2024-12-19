package com.szylas.medmemo

import com.szylas.medmemo.calendar.domain.generateDates
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Calendar

class CalendarTests {

    @Test
    fun generateCalendarsValid() {
        val start = Calendar.getInstance()
        val count = 14

        val dates = generateDates(start, count)

        assert(dates.map {
            Triple(
                it.get(Calendar.DATE),
                it.get(Calendar.MONTH),
                it.get(Calendar.YEAR)
            )
        }.distinct().size == count)
    }

    @Test
    fun generateCalendarsNegativeCount() {
        val start = Calendar.getInstance()
        val count = -1

        assertThrows(IllegalArgumentException::class.java) { generateDates(start, count) }
    }

    @Test
    fun generateCalendarsZeroCount() {
        val start = Calendar.getInstance()
        val count = 0

        assertThrows(IllegalArgumentException::class.java) { generateDates(start, count) }
    }

    @Test
    fun generateCalendarsOneCount() {
        val start = Calendar.getInstance()
        val count = 1

        assertThrows(IllegalArgumentException::class.java) { generateDates(start, count) }
    }

    @Test
    fun generateCalendarsTwoCount() {
        val start = Calendar.getInstance()
        val count = 2

        val dates = generateDates(start, count)

        assert(dates.map {
            Triple(
                it.get(Calendar.DATE),
                it.get(Calendar.MONTH),
                it.get(Calendar.YEAR)
            )
        }.distinct().size == count)
    }
}