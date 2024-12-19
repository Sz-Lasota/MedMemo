package com.szylas.medmemo

import com.szylas.medmemo.common.domain.formatters.formatDate
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.statistics.domain.StatisticsManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar


class StatisticsTests {



    private lateinit var memos: List<Memo>
    private lateinit var weeklyMemos: List<Memo>

    @Before
    fun provideMemosForOneDay() {

        memos = listOf(
            Memo(
                notifications = mutableListOf(
                    MemoNotification(
                        date = Calendar.getInstance(),
                        intakeTime = Calendar.getInstance()
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) },
                        intakeTime = Calendar.getInstance()
                    ),
                )
            ),
            Memo(
                notifications = mutableListOf(
                    MemoNotification(date = Calendar.getInstance()),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) },
                        intakeTime = Calendar.getInstance()
                    ),
                )
            ),
            Memo(
                notifications = mutableListOf(
                    MemoNotification(date = Calendar.getInstance()),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }),
                )
            ),
            Memo(
                notifications = mutableListOf(
                    MemoNotification(
                        date = Calendar.getInstance(),
                        intakeTime = Calendar.getInstance()
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }),
                )
            ),
        )
    }

    @Before
    fun provideMemosForWeek() {

        val week = listOf(
            Calendar.getInstance().apply { add(Calendar.DATE, -7) },
            Calendar.getInstance().apply { add(Calendar.DATE, -6) },
            Calendar.getInstance().apply { add(Calendar.DATE, -5) },
            Calendar.getInstance().apply { add(Calendar.DATE, -4) },
            Calendar.getInstance().apply { add(Calendar.DATE, -3) },
            Calendar.getInstance().apply { add(Calendar.DATE, -2) },
            Calendar.getInstance().apply { add(Calendar.DATE, -1) },
            Calendar.getInstance(),
        )
        weeklyMemos = listOf(
            Memo(
                name = "First",
                startDate = Calendar.getInstance().apply { timeInMillis = week[0].timeInMillis },
                notifications = mutableListOf(
                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[0].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[0].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[0].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[0].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[1].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[1].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[1].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[1].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[2].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[2].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[2].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[2].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[3].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[3].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[3].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[3].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[4].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[4].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[4].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[4].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[5].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[5].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[5].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[5].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[6].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[6].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[6].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[6].timeInMillis }
                    ),

                    MemoNotification(
                        date = Calendar.getInstance().apply { timeInMillis = week[7].timeInMillis },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[7].timeInMillis }
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply {
                            timeInMillis = week[7].timeInMillis; add(
                            Calendar.HOUR_OF_DAY,
                            1
                        )
                        },
                        intakeTime = Calendar.getInstance()
                            .apply { timeInMillis = week[7].timeInMillis }
                    ),
                )
            ),
            Memo(
                name = "Second",
                startDate = Calendar.getInstance().apply { timeInMillis = week[7].timeInMillis },
                notifications = mutableListOf(
                    MemoNotification(date = Calendar.getInstance()),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) },
                        intakeTime = Calendar.getInstance()
                    ),
                )
            ),
            Memo(
                name = "Third",
                startDate = Calendar.getInstance().apply { add(Calendar.DATE, 1) },

                notifications = mutableListOf(
                    MemoNotification(date = Calendar.getInstance()),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }),
                )
            ),
            Memo(
                name = "Fourth",
                startDate = Calendar.getInstance().apply { add(Calendar.DATE, 1) },

                notifications = mutableListOf(
                    MemoNotification(
                        date = Calendar.getInstance(),
                        intakeTime = Calendar.getInstance()
                    ),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }),
                )
            ),
        )
    }

    @Test
    fun pillStatusFull() {
        val manager = StatisticsManager(null)
        manager.memos = memos

        val actual = manager.pillsStatus(Calendar.getInstance())

        assertEquals(listOf(4, 8), actual)
    }

    @Test
    fun pillStatusEmpty() {
        val manager = StatisticsManager(null)
        manager.memos = listOf()

        val actual = manager.pillsStatus(Calendar.getInstance())

        assertEquals(listOf(0, 0), actual)
    }

    @Test
    fun acticeWeekly() {
        val manager = StatisticsManager(null)
        manager.memos = weeklyMemos

        val actual = manager.active()

        assertEquals(2, actual)
    }

    @Test
    fun acticeWeeklyEmpty() {
        val manager = StatisticsManager(null)
        manager.memos = listOf()

        val actual = manager.active()

        assertEquals(0, actual)
    }

    @Test
    fun namesWeekly() {
        val manager = StatisticsManager(null)
        manager.memos = weeklyMemos

        val actual = manager.names()

        assertEquals(listOf("First"), actual)
    }

    @Test
    fun namesEmpty() {
        val manager = StatisticsManager(null)
        manager.memos = listOf()

        val actual = manager.names()

        assertEquals(listOf<String>(), actual)
    }

    @Test
    fun pillsTimeWeekly() {
        val manager = StatisticsManager(null)
        manager.memos = weeklyMemos
        val startDate = Calendar.getInstance()

        val actual = manager.pillsTime("First", Calendar.getInstance())

        val filter = weeklyMemos[0].notifications
            .filter {
                it.date.after(Calendar.getInstance().apply {
                    timeInMillis = startDate.timeInMillis
                    add(Calendar.DATE, -5)
                    set(Calendar.HOUR_OF_DAY, 1)
                })
                        && it.date.before(Calendar.getInstance().apply {
                    timeInMillis = startDate.timeInMillis
                    add(Calendar.DATE, 1)
                    set(Calendar.HOUR_OF_DAY, 1)
                })
            }
        assertEquals(Pair(
            mutableListOf(
                filter
                    .map { it.intakeTime }
                    .map {
                        it!!.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE).toDouble()
                    }
            ), listOf(filter.map { formatDate(it.date) }, listOf("00:00"))
        ), actual)
    }

    @Test
    fun pillsTimeEmpty() {
        val manager = StatisticsManager(null)
        manager.memos = listOf(Memo(name = "First"))

        val actual = manager.pillsTime("First", Calendar.getInstance())

        assertEquals(Pair(listOf<Double>(), listOf(listOf(), listOf<String>())), actual)
    }

    @Test
    fun pillsTimeAbsent() {
        val manager = StatisticsManager(null)
        manager.memos = listOf()

        val actual = manager.pillsTime("First")

        assertEquals(Pair(listOf<Double>(), listOf<List<String>>()), actual)
    }


    @Test
    fun missedWeekly() {
        val manager = StatisticsManager(null)
        manager.memos = weeklyMemos

        val actual = manager.missed()

        assertEquals(Pair(0.0, 12.0), actual)
    }

    @Test
    fun missedEmpty() {
        val manager = StatisticsManager(null)
        manager.memos = listOf()

        val actual = manager.missed()

        assertEquals(Pair(0.0, 0.0), actual)
    }
}