package com.szylas.medmemo.calendar.presentation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.szylas.medmemo.calendar.domain.generateDates
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.common.domain.models.TreatmentModel
import com.szylas.medmemo.calendar.domain.models.TreatmentScheduleItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun DayCarousel(activity: ComponentActivity) {
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
                month = SimpleDateFormat("MMM", Locale.getDefault()).format(it.time),
                active = selectedIndex == index
            ) {
                if (index == selectedIndex) {
                    return@DateTile
                }
                Toast.makeText(
                    activity, "Hello from: ${
                        SimpleDateFormat(
                            "dd-MMM", Locale.getDefault()
                        ).format(it.time)
                    }", Toast.LENGTH_SHORT
                ).show()
                selectedIndex = index
            }
        }

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
            .clip(RoundedCornerShape(34.dp)),
//            .background(MaterialTheme.colorScheme.secondary),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = eventTitle,
            style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE),
//            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun EventTile(activity: ComponentActivity, event: TreatmentModel, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp)),
//            .background(MaterialTheme.colorScheme.primary),
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
//                color = MaterialTheme.colorScheme.onPrimary
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
            if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary
        ), shape = RoundedCornerShape(20.dp), colors = ButtonColors(
            containerColor = if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary,
            contentColor = if (active) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
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
//                color = if (active) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary
            )
            Text(
                text = month,
                style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
//                color = if (active) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}