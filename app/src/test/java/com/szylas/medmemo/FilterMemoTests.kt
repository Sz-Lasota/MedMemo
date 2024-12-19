package com.szylas.medmemo

import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.main.domain.getUpcomingNotifications
import org.junit.Test
import org.junit.Assert.assertEquals
import java.util.Calendar

class FilterMemoTests {

    @Test
    fun filterEmptyMemos() {
        val memos: List<Memo> = listOf()

        val actual = getUpcomingNotifications(memos)

        assertEquals(actual, listOf<Pair<Memo, MemoNotification>>())
    }

    @Test
    fun filterMemosFromTomorrow() {
        val memos = listOf(
            Memo(
                notifications = mutableListOf(
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
                        intakeTime = Calendar.getInstance()
                    ),
                    MemoNotification(
                        date = Calendar.getInstance()
                            .apply { add(Calendar.DATE, 1); add(Calendar.HOUR_OF_DAY, 1) },
                        intakeTime = Calendar.getInstance()
                    ),
                )
            ),
            Memo(
                notifications = mutableListOf(
                    MemoNotification(date = Calendar.getInstance().apply { add(Calendar.DATE, 1) }),
                    MemoNotification(
                        date = Calendar.getInstance()
                            .apply { add(Calendar.DATE, 1); add(Calendar.HOUR_OF_DAY, 1) },
                        intakeTime = Calendar.getInstance()
                    ),
                )
            ),
            Memo(
                notifications = mutableListOf(
                    MemoNotification(date = Calendar.getInstance().apply { add(Calendar.DATE, 1) }),
                    MemoNotification(
                        date = Calendar.getInstance()
                            .apply { add(Calendar.DATE, 1); add(Calendar.HOUR_OF_DAY, 1) }),
                )
            ),
        )

        val actual = getUpcomingNotifications(memos)

        assertEquals(actual, listOf<Pair<Memo, MemoNotification>>())
    }

    @Test
    fun filterMemosFromToday() {
        val memos = listOf(
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
                    MemoNotification(date = Calendar.getInstance(), intakeTime = Calendar.getInstance()),
                    MemoNotification(
                        date = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }),
                )
            ),
        )

        val actual = getUpcomingNotifications(memos)

        assertEquals(actual, listOf(
            Pair(memos[1], memos[1].notifications[0]),
            Pair(memos[2], memos[2].notifications[0]),
            Pair(memos[3], memos[3].notifications[1]),
        ))
    }

}