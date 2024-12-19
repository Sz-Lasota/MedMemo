package com.szylas.medmemo

import com.szylas.medmemo.common.domain.formatters.formatDate
import com.szylas.medmemo.common.domain.formatters.formatFullDate
import com.szylas.medmemo.common.domain.formatters.formatTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class FormattersTests {

    @Test
    fun formatDateOneDigitDay() {
        Locale.setDefault(Locale.ENGLISH)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, 5)
            set(Calendar.DAY_OF_MONTH, 5)
        }

        val actual = formatDate(calendar)

        assertEquals("05-Jun", actual)
    }

    @Test
    fun formatDateTwoDigitDay() {
        Locale.setDefault(Locale.ENGLISH)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, 5)
            set(Calendar.DAY_OF_MONTH, 11)
        }

        val actual = formatDate(calendar)

        assertEquals("11-Jun", actual)
    }

    @Test
    fun formatFullDateTwoDigit() {
        Locale.setDefault(Locale.ENGLISH)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, 5)
            set(Calendar.DAY_OF_MONTH, 11)
            set(Calendar.YEAR, 2024)
        }

        val actual = formatFullDate(calendar)

        assertEquals("11/Jun/2024", actual)
    }

    @Test
    fun formatFullDateOneDigit() {
        Locale.setDefault(Locale.ENGLISH)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, 5)
            set(Calendar.DAY_OF_MONTH, 5)
            set(Calendar.YEAR, 2024)
        }

        val actual = formatFullDate(calendar)

        assertEquals("05/Jun/2024", actual)
    }

    @Test
    fun formatTimeFromIntMidnight() {
        val time = 0

        val actual = formatTime(time)

        assertEquals("00:00", actual)
    }

    @Test
    fun formatTimeFromIntOneToMidnight() {
        val time = 23*60+59

        val actual = formatTime(time)

        assertEquals("23:59", actual)
    }

    @Test
    fun formatTimeFromIntQuaterPastFour() {
        val time = 4*60+15

        val actual = formatTime(time)

        assertEquals("04:15", actual)
    }

    @Test
    fun formatTimeFromIntFiveToSeventeen() {
        val time = 16*60+55

        val actual = formatTime(time)

        assertEquals("16:55", actual)
    }

    @Test
    fun formatTimeFromIntNegative() {
        val time = -10

        assertThrows(IllegalArgumentException::class.java) {
            formatTime(time)
        }
    }

    @Test
    fun formatTimeFromIntOverflow() {
        val time = 24*60

        assertThrows(IllegalArgumentException::class.java) {
            formatTime(time)
        }
    }

    @Test
    fun formatTimeFromCalendarOneDigit() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 4)
            set(Calendar.MINUTE, 4)
        }

        val actual = formatTime(calendar)

        assertEquals("04:04", actual)
    }

    @Test
    fun formatTimeFromCalendarTwoDigits() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 36)
        }

        val actual = formatTime(calendar)

        assertEquals("14:36", actual)
    }
}