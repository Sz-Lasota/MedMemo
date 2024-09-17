package com.szylas.medmemo.statistics.presentation

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line


@Composable
fun LineChartTile(data: List<List<Double>>, labels: List<String>, title: String, modifier: Modifier = Modifier) {
    val dataList = data
        .map {
            Line(
                label = "",
                values = it,
                color = SolidColor(Color.Red),
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(Color.Red)
                ),
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
            )
        }

    LineChart(
        modifier = modifier,
        labelProperties = LabelProperties(
            enabled = true,
            labels = labels
        ),
        data = dataList,
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        minValue = data.flatten().minOrNull() ?: 0.0
    )
}
