package com.szylas.medmemo.memo.domain.predictions

import android.util.Log
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar

class WeightedAveragePrediction : IPrediction {
    override fun predict(
        predictionNotification: MemoNotification,
        previousData: List<MemoNotification>
    ): Int {
        Log.d("Rescheduling", "Rescheduling memo: ${predictionNotification.name}")
        // It there is not enough data, default value is returned
        if (previousData.size < 7) {
            return predictionNotification.baseDosageTime
        }

        val lookUpDate = predictionNotification.date.get(Calendar.DAY_OF_WEEK)
        val newTime = previousData
            .mapNotNull { it.intakeTime }
            .filter { it.get(Calendar.DAY_OF_WEEK) == lookUpDate }
            .map { it.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE) }
            .average()
            .toInt()

        return newTime
    }
}