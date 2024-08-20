package com.szylas.medmemo.memo.domain.predictions

import com.szylas.medmemo.common.domain.models.MemoNotification
import java.util.Calendar

interface IPrediction {

    fun predict(
        predictionNotification: MemoNotification,
        previousData: List<MemoNotification>
    ): Int
}