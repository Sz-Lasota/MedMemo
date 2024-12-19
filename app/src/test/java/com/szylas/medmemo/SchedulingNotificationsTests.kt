package com.szylas.medmemo

import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.memo.domain.predictions.WeightedAveragePrediction
import org.junit.Test

import org.junit.Assert.*
import java.util.Calendar

class SchedulingNotificationsTests {

    @Test
    fun schedulingNotification_notEnoughData() {
        val prediction = WeightedAveragePrediction()
        val baseTime = 640
        val notification = MemoNotification(baseDosageTime = baseTime)

        val actual = prediction.predict(
            notification,
            listOf()
        )

        assertEquals(baseTime, actual)
    }

    @Test
    fun schedulingNotification_correctReshedule15MinLater() {
        val prediction = WeightedAveragePrediction()
        val baseTime = 640
        val expected = 8 * 60 + 15
        val notification = MemoNotification(
            baseDosageTime = baseTime,
            date = Calendar.getInstance()
                .apply { timeInMillis = 1733641200000 }
        )
        val history = listOf(
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733036400000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733037300000 }
            ),
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733122800000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733122800000 }
            ),
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733209200000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733209200000 }
            ),
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733295600000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733295600000 }
            ),
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733382000000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733382000000 }
            ),
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733468400000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733468400000 }
            ),
            MemoNotification(
                date = Calendar.getInstance()
                    .apply { timeInMillis = 1733554800000 },
                intakeTime = Calendar.getInstance()
                    .apply { timeInMillis = 1733554800000 }
            ),
        )

        val actual = prediction.predict(
            notification,
            history
        )

        assertEquals(expected, actual)
    }

    @Test
    fun schedulingNotification_notTakenThisDay() {
        val prediction = WeightedAveragePrediction()
        val baseTime = 640
        val notification = MemoNotification(baseDosageTime = baseTime, date = Calendar.getInstance().apply { timeInMillis = 1733641200000 })

        val notificationHistory = listOf(
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733036400000 }, intakeTime = null),
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733122800000 }, intakeTime = Calendar.getInstance().apply { timeInMillis = 1733122800000 }),
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733209200000 }, intakeTime = Calendar.getInstance().apply { timeInMillis = 1733209200000 }),
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733295600000 }, intakeTime = Calendar.getInstance().apply { timeInMillis = 1733295600000 }),
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733382000000 }, intakeTime = Calendar.getInstance().apply { timeInMillis = 1733382000000 }),
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733468400000 }, intakeTime = Calendar.getInstance().apply { timeInMillis = 1733468400000 }),
            MemoNotification(date = Calendar.getInstance().apply { timeInMillis = 1733554800000 }, intakeTime = Calendar.getInstance().apply { timeInMillis = 1733554800000 }),
        )

        val actual = prediction.predict(
            notification,
            notificationHistory
        )

        assertEquals(baseTime, actual)

    }
    
}