package com.szylas.medmemo.calendar.domain.models

import com.szylas.medmemo.common.domain.models.TreatmentModel

data class TreatmentScheduleItem(
    val time: String,
    val events: List<TreatmentModel>
)