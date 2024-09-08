package com.szylas.medmemo.main.presentation.views

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.calendar.presentation.DayCarousel
import com.szylas.medmemo.calendar.presentation.DaySchedule
import com.szylas.medmemo.common.domain.models.Memo
import java.util.Calendar

@Composable
fun CalendarFragment(memos: List<Memo>?, activity: ComponentActivity) {
    if (memos == null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
            )
        }
        return
    }


    var lookupDay by remember {
        mutableStateOf(Calendar.getInstance())
    }

    var notifications by remember {
        mutableStateOf(
            memos
                .flatMap { it.notifications }
                .filter {
                    it.date.get(Calendar.DAY_OF_YEAR) == lookupDay.get(Calendar.DAY_OF_YEAR)
                            && it.date.get(Calendar.YEAR) == lookupDay.get(Calendar.YEAR)
                }
        )
    }

    Log.d("Notifications", notifications.size.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
    ) {
        DayCarousel(activity) { calendar ->
            lookupDay = calendar
            notifications = memos
                .flatMap { it.notifications }
                .filter {
                    it.date.get(Calendar.DAY_OF_YEAR) == lookupDay.get(Calendar.DAY_OF_YEAR)
                            && it.date.get(Calendar.YEAR) == lookupDay.get(Calendar.YEAR)
                }
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .padding(10.dp),
        ) {
            DaySchedule(
                activity = activity,
                modifier = Modifier.fillMaxWidth(),
                scheduledEvents = notifications
            )
        }
    }
}
