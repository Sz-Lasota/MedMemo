package com.szylas.medmemo.main.presentation.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.statistics.datastore.MissedStatistics
import com.szylas.medmemo.statistics.datastore.TimeStatistics
import com.szylas.medmemo.statistics.domain.ChartProvider
import com.szylas.medmemo.statistics.presentation.ChartTile

// TODO: Refactor all of this:
//  1. Encapsulate wantedStats in some wrapper class that will also handle title and plot type
//  2. Statistical interface should not return List<Point> but raw data from db
//  3. Chart provider should be replaced by some interface, probably included in wrapper from pt. 1
//      and that how charts should be generated and plot type chosen.

val wantedStats = listOf(
    TimeStatistics(),
    MissedStatistics(),
    TimeStatistics(),
    MissedStatistics(),
    TimeStatistics(),
    MissedStatistics(),
)

@Suppress("UNUSED_PARAMETER")
@Composable
fun StatisticsFragment(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.statistics),
            style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE)
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(wantedStats) {
                ChartTile(chart = ChartProvider.provide(it), title = it.title)
            }
        }
    }
}