package com.szylas.medmemo.common.presentation.views

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.common.datastore.viewhelpers.generateDates
import com.szylas.medmemo.common.presentation.models.TreatmentModel
import com.szylas.medmemo.common.presentation.models.TreatmentScheduleItem
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
        var selectedIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            val start = Calendar.getInstance()
            itemsIndexed(generateDates(start, 14)) { index, it ->
                DateTile(
                    day = it.get(Calendar.DAY_OF_MONTH).toString(),
                    month = SimpleDateFormat("MMM", Locale.getDefault()).format(it.getTime()),
                    active = selectedIndex == index
                ) {
                    if (index == selectedIndex) {
                        return@DateTile
                    }
                    Toast.makeText(
                        activity, "Hello from: ${
                            SimpleDateFormat(
                                "DD-MMM", Locale.getDefault()
                            ).format(it.time)
                        }", Toast.LENGTH_SHORT
                    ).show()
                    selectedIndex = index
                }
            }

        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySchedule(activity: ComponentActivity, scheduledEvents: List<TreatmentScheduleItem>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        scheduledEvents.forEach { eventPack ->
            stickyHeader {
                EventHeader(eventTitle = eventPack.time, modifier = Modifier.fillMaxWidth())
            }
            items(eventPack.events) { event ->
                    EventTile(activity = activity, event = event, modifier = Modifier.fillMaxWidth(0.9f))
            }
        }

    }
}

@Composable
fun EventHeader(eventTitle: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(34.dp))
            .background(MaterialTheme.colorScheme.secondary),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = eventTitle,
            style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun EventTile(activity: ComponentActivity, event: TreatmentModel, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primary),
        onClick = { Toast.makeText(activity, event.title, Toast.LENGTH_LONG).show() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.Start),
        ) {
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                text = event.title,
                style = TextStyleProvider.provide(style = TextStyleOption.LABEL_LARGE),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DateTile(day: String, month: String, active: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier
            .width(80.dp)
            .height(70.dp), border = BorderStroke(
            1.dp,
            if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        ), shape = RoundedCornerShape(20.dp), colors = ButtonColors(
            containerColor = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (active) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary,
            disabledContainerColor = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day,
                style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
                color = if (active) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary
            )
            Text(
                text = month,
                style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
                color = if (active) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}