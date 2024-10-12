package com.szylas.medmemo.calendar.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.calendar.domain.generateDates
import com.szylas.medmemo.common.domain.formatters.formatTime
import com.szylas.medmemo.common.domain.models.MemoNotification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun DayCarousel(activity: ComponentActivity, onItemClick: (Calendar) -> Unit) {
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        val start = Calendar.getInstance()
        itemsIndexed(generateDates(start, 14)) { index, calendar ->
            DateTile(
                day = calendar.get(Calendar.DAY_OF_MONTH).toString(),
                month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time),
                active = selectedIndex == index
            ) {
                if (index == selectedIndex) {
                    return@DateTile
                }
                onItemClick(calendar)
                selectedIndex = index
            }
        }

    }
}

@Composable
fun DaySchedule(
    activity: ComponentActivity,
    scheduledEvents: List<MemoNotification>,
    modifier: Modifier = Modifier,
) {
    val columnsCount = scheduledEvents
        .map { it.date.get(Calendar.HOUR_OF_DAY) * 60 + it.date.get(Calendar.MINUTE) }
        .groupBy { it }
        .map { it.value.size }
        .maxOrNull()

    val gridCells = scheduledEvents
            .groupBy { it.date.get(Calendar.HOUR_OF_DAY) * 60 + it.date.get(Calendar.MINUTE) }
            .toList()
            .sortedBy { it.second.first().date }

    val cellHeight = 70.dp

    if (columnsCount == null) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.there_are_no_notifications_scheduled))
        }
        return
    }

    Row(
        modifier = modifier
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(columnsCount),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            gridCells.forEachIndexed { index, (time, notifications) ->
                items(notifications) {
                    NotificationEvent(
                        it,
                        modifier = Modifier
                            .height(cellHeight)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(10.dp)
                    )
                }
                items(columnsCount - notifications.size) {
                    EmptyEvent(
                        modifier = Modifier
                            .height(cellHeight)
                            .fillMaxWidth()
                    )
                }

                if (index >= gridCells.size - 1) {
                    return@forEachIndexed
                }
                items(columnsCount) { id ->
                    val gapInMillis = gridCells[index + 1].first - time
                    val gapInQuarters = gapInMillis / 30
                    if (id == 0) {
                        TimedEvent(
                            time = time + 30,
                            modifier = Modifier
                                .height((cellHeight.value * gapInQuarters).dp)
                                .fillMaxWidth()
                        )
                    } else {
                        EmptyEvent(
                            modifier = Modifier
                                .height((cellHeight.value * gapInQuarters).dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimedEvent(time: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(formatTime(time))
    }
}

@Composable
fun EmptyEvent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {

    }
}

@Composable
fun NotificationEvent(notification: MemoNotification, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(text = notification.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, color = MaterialTheme.colorScheme.onPrimary)
        Text(text = formatTime(notification.date), maxLines = 1, color = MaterialTheme.colorScheme.onPrimary)

    }
}

@Composable
fun DateTile(day: String, month: String, active: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(80.dp)
            .height(70.dp),
        shape = RoundedCornerShape(20.dp),
        enabled = !active,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = ButtonDefaults.buttonColors().containerColor,
            disabledContentColor = ButtonDefaults.buttonColors().contentColor
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day,
            )
            Text(
                text = month,
            )
        }
    }
} 