package com.szylas.medmemo.memo.domain.predictions

import android.util.Log
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar

class WeightedAveragePrediction : IPrediction {
    override fun predict(
        predictionNotification: MemoNotification,
        previousData: List<MemoNotification>,
    ): Int {
        // It there is not enough data, default value is returned
        if (previousData.size < 7) {
            return predictionNotification.baseDosageTime
        }

        val lookUpDate = predictionNotification.date.get(Calendar.DAY_OF_WEEK)

        val previousHours = previousData
            .mapNotNull { it.intakeTime }
            .filter { it.get(Calendar.DAY_OF_WEEK) == lookUpDate }
            .associateWith { it.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE) }

        if (previousHours.isEmpty()) {
            return predictionNotification.baseDosageTime
        }

        val recent = previousHours.keys
            .map { it.timeInMillis }
            .sorted()
            .takeLast(3)

        val numerator = previousHours
            .entries
            .sumOf { (k, v) -> if (recent.contains(k.timeInMillis)) v * 3 else v }

        val denominator = 3 * recent.size + (previousHours.size - recent.size)

        return numerator / denominator
    }
}