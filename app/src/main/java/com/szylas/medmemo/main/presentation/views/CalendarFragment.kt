package com.szylas.medmemo.main.presentation.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.calendar.presentation.DayCarousel
import com.szylas.medmemo.calendar.presentation.DaySchedule
import com.szylas.medmemo.common.domain.models.TreatmentModel
import com.szylas.medmemo.calendar.domain.models.TreatmentScheduleItem


val mock = listOf(
    TreatmentModel("First", 1, 8 * 60 + 0),
    TreatmentModel("Second", 2, 8 * 60 + 0),
    TreatmentModel("First", 3, 9 * 60 + 0),
    TreatmentModel("First", 1, 8 * 60 + 0),
    TreatmentModel("First", 1, 9 * 60 + 30),
    TreatmentModel("First", 1, 8 * 60 + 0),
    TreatmentModel("First", 1, 8 * 60 + 0),
    TreatmentModel("First", 1, 12 * 60 + 0),
    TreatmentModel("First", 1, 20 * 60 + 45),
).groupBy {
    it.timeString()
}.toSortedMap()

@Composable
fun CalendarFragment(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
    ) {
        DayCarousel(activity)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = MaterialTheme.colorScheme.primary
        )

        DaySchedule(activity = activity, modifier = Modifier.fillMaxWidth(), scheduledEvents = mock.map {
            TreatmentScheduleItem(
                time = it.key, events = it.value
            )
        })

    }
}
