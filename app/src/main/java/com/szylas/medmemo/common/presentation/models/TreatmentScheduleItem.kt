package com.szylas.medmemo.common.presentation.models

data class TreatmentScheduleItem(
    val time: String,
    val events: List<TreatmentModel>
)