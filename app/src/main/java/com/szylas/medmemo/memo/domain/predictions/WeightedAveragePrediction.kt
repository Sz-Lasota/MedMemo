package com.szylas.medmemo.memo.domain.predictions

import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar

class WeightedAveragePrediction : IPrediction {
    override fun predict(
        nextNotification: MemoNotification,
        previousData: List<MemoNotification>,
    ): Int {
        // It there is not enough data, default value is returned
        if (previousData.size < 7) {
            return nextNotification.baseDosageTime
        }

        // Select day of the week for which notification will be scheduled
        val lookUpDate = nextNotification.date.get(Calendar.DAY_OF_WEEK)

        // Filter existing data to include only non-null values from correct day
        val previousHours = previousData
            .mapNotNull { it.intakeTime }
            .filter { it.get(Calendar.DAY_OF_WEEK) == lookUpDate }
            .associateWith { it.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE) }

        if (previousHours.isEmpty()) {
            return nextNotification.baseDosageTime
        }

        // Assign weights
        val high = previousHours.keys
            .map { it.timeInMillis }
            .sorted()
            .takeLast(3)

        val mid = previousHours.keys
            .map { it.timeInMillis }
            .sorted()
            .takeLast(12)
            .take(9)
            .filter { !high.contains(it) }

        // Calculate numerator and denominator
        val numerator = previousHours
            .entries
            .sumOf { (k, v) ->
                if (high.contains(k.timeInMillis))
                    v * 3
                else if (mid.contains(k.timeInMillis))
                    2 * v
                else v
            }

        val denominator = 3 * high.size +
                2 * mid.size +
                (previousHours.size - high.size - mid.size)

        return numerator / denominator
    }
}