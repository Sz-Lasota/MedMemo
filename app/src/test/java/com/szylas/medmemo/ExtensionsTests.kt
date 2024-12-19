package com.szylas.medmemo

import androidx.compose.ui.graphics.Color
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.memo.datastore.models.PillCount
import com.szylas.medmemo.memo.domain.extensions.color
import com.szylas.medmemo.memo.domain.extensions.generateNotifications
import com.szylas.medmemo.memo.domain.extensions.id
import com.szylas.medmemo.memo.domain.extensions.name
import com.szylas.medmemo.memo.domain.extensions.provideTimes
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class ExtensionsTests {

    @Test
    fun provideTimesTwoGapThree() {
        val memo = Memo(numberOfDoses = 2, gap = 3 * 60)

        val actual = memo.provideTimes()

        assertEquals(actual, listOf(8 * 60, 11 * 60))
    }

    @Test
    fun provideTimesOne() {
        val memo = Memo(numberOfDoses = 1)

        val actual = memo.provideTimes()

        assertEquals(actual, listOf(8 * 60))
    }

    @Test
    fun provideId() {
        val start = Calendar.getInstance()
        val memo = Memo(name = "Test", startDate = start)

        val actual = memo.id()

        assertEquals(actual, "Test_${start.timeInMillis}")
    }

    @Test
    fun generateNotifications() {
        val dosageTime = listOf(8 * 60, 12 * 60, 15 * 60)
        val memo = Memo(dosageTime = dosageTime)

        memo.generateNotifications()

        assert(memo.notifications.size == dosageTime.size && memo.notifications.mapIndexed { index, notif ->
            notif.date.get(Calendar.HOUR_OF_DAY) * 60 + notif.date.get(
                Calendar.MINUTE
            ) == dosageTime[index]
        }.all { it })
    }

    @Test
    fun generateNotificationsNull() {
        val dosageTime: List<Int> = listOf()
        val memo = Memo(dosageTime = dosageTime)

        memo.generateNotifications()

        assert(memo.notifications.size == 0)
    }

    @Test
    fun getPillCountId() {
        val pillCount = PillCount("test_id")

        val actual = pillCount.name()

        assertEquals(actual, "test")
    }

    @Test
    fun getPillCountIdDouble_() {
        val pillCount = PillCount("test_id_id")

        val actual = pillCount.name()

        assertEquals(actual, "test")
    }

    @Test
    fun getPillCountColorGreen() {
        val pillCount = PillCount(count = 6, maxAmount = 6)

        val actual = pillCount.color()

        assertEquals(actual, Color(0xFF2F842E))
    }

    @Test
    fun getPillCountColorOrange() {
        val pillCount = PillCount(count = 2, maxAmount = 6)

        val actual = pillCount.color()

        assertEquals(actual, Color(0xFFC89222))
    }

    @Test
    fun getPillCountColorRed() {
        val pillCount = PillCount(count = 1, maxAmount = 6)

        val actual = pillCount.color()

        assertEquals(actual, Color(0xFFB13434))
    }
}