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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import kotlin.math.sign

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
                                    .fillMaxSize(),
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
                if (statisticsManager.names().isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(5.dp, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)

                    ) {
                        Text(text = stringResource(R.string.there_are_no_active_therapies))
                    }
                } else {
                    PillTimeChart()
                }
            }

            item {
                val pillCount by remember {
                    mutableStateOf(statisticsManager.missed())
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp)
                        .height(210.dp)

                ) {
                    Text(
                        text = stringResource(R.string.adherence),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    if (pillCount.first + pillCount.second < 3) {
                        Text(
                            text = stringResource(id = R.string.there_are_no_active_therapies),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = "Taken: ", color = Color(0xff449e48))
                            Text(text = "${pillCount.second.toInt()}")
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Missed: ", color = Color(0xffC41E3A))
                            Text(text = "${pillCount.first.toInt()}")
                        }
                        PieChartTile(
                            data = listOf(pillCount.first, pillCount.second),
                            labels = listOf(
                                stringResource(R.string.missed), stringResource(R.string.taken)
                            ),
                            colors = listOf(
                                Color(0xffC41E3A),
                                Color(0xff449e48),
                                ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                }
            }
        }
    }


    @Composable
    private fun PillTimeChart() {
        val treatments by remember {
            mutableStateOf(statisticsManager.names())
        }

        var currentTreatment by remember {
            mutableIntStateOf(0)
        }

        var lookupTreatment by remember {
            mutableStateOf(statisticsManager.names().first())
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp)
                .height(210.dp)

        ) {
            val data = statisticsManager.pillsTime(lookupTreatment)


            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = {
                    currentTreatment =
                        if (currentTreatment == 0) treatments.size - 1 else currentTreatment - 1
                    lookupTreatment = treatments[currentTreatment]
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Left"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = treatments[currentTreatment],
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    currentTreatment = (currentTreatment + 1) % treatments.size
                    lookupTreatment = treatments[currentTreatment]
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Right"
                    )
                }

            }
            if (data.first.isEmpty() || data.first.any { it.size < 2 }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.not_enough_data),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            } else {
                LineChartTile(
                    data = data.first,
                    labels = data.second[0],
                    titles = data.second[1],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                )
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