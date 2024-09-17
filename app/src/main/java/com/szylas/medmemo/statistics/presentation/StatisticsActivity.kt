package com.szylas.medmemo.statistics.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.common.presentation.theme.imageBackground
import com.szylas.medmemo.statistics.datastore.FirebaseMemoRepository
import com.szylas.medmemo.statistics.domain.StatisticsManager
import com.szylas.medmemo.statistics.presentation.components.ProgressCircle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

class StatisticsActivity : ComponentActivity() {

    private val statisticsManager = StatisticsManager(FirebaseMemoRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()

        setContent {
            var fetched by remember {
                mutableStateOf(false)
            }

            lifecycleScope.update {
                fetched = it
            }

            MedMemoTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .paint(
                                imageBackground(),
                                contentScale = ContentScale.FillBounds
                            )
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        if (!fetched) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(64.dp),
                                )
                            }
                        } else {
                            LazyStats(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            )

                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LazyStats(modifier: Modifier = Modifier) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    val pills = statisticsManager.pillsStatus(Calendar.getInstance())
                    PillsStatus(
                        pillsTaken = pills[0],
                        pillsTotal = pills[1],
                        modifier = Modifier
                            .weight(1f)
                    )

                    TherapiesNumber(
                        number = statisticsManager.active(),
                        modifier = Modifier
                            .weight(1f)
                    )

                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp)
                        .height(210.dp)

                ) {
                    val data = statisticsManager.pillsTime()
                    LineChartTile(
                        data = data.first,
                        title = "Apap",
                        labels = data.second,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun TherapiesNumber(number: Int, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.active_therapies),
                style = MaterialTheme.typography.headlineSmall,
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Text(
                text = "$number"
            )

        }
    }

    // Small item
    @Composable
    private fun PillsStatus(pillsTaken: Int, pillsTotal: Int, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.today_pills),
                style = MaterialTheme.typography.headlineSmall,
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            ProgressCircle(
                currentValue = pillsTaken,
                maxValue = pillsTotal,
                radius = 56.dp,
                progressBackgroundColor = MaterialTheme.colorScheme.surface,
                completedColor = MaterialTheme.colorScheme.primary,
                progressIndicatorColor = MaterialTheme.colorScheme.primary
            )

        }
    }

    private fun CoroutineScope.update(callback: (Boolean) -> Unit) = launch {
        statisticsManager.update(callback)
    }
}