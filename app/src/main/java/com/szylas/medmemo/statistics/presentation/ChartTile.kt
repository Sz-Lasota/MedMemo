package com.szylas.medmemo.statistics.presentation

import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.szylas.medmemo.common.domain.formatters.formatTime
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.Pie
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle

private val colors = listOf(
    Color(0xffC41E3A),
    Color(0xff449e48),
    Color.DarkGray,
    Color.Green,
    Color.Magenta,
    Color.DarkGray
)


@Composable
fun LineChartTile(data: List<List<Double>>, labels: List<String>, titles: List<String>, modifier: Modifier = Modifier) {
    val dataList = data
        .mapIndexed { index, it ->
            Line(
                label = titles[index],
                curvedEdges = false,
                values = it,
                popupProperties = PopupProperties(
                    enabled = false
                ),
                color = SolidColor(colors[index % colors.size]),
                dotProperties = DotProperties(
                    enabled = true,
                    color = SolidColor(colors[index % colors.size])
                ),
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 0.dp, strokeStyle = StrokeStyle.Dashed(
                    floatArrayOf(5f, 10f)
                )),
            )
        }

    var minValue = data.flatten().min() - 1.0
    if (minValue < 0) {
        minValue = 0.0
    }
    LineChart(
        modifier = modifier,
        dividerProperties = DividerProperties(
            enabled = true,
            xAxisProperties = LineProperties(
                enabled = false
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            contentBuilder = {
                Log.d("TimeLabel", it.toString())
                val hour = it.toInt()
                val minute = ((it - hour) * 100).toInt()
                if (minute >= 60) {
                    ""
                } else {
                    formatTime(hour * 60 + minute)
                }
            }
        ),
        labelProperties = LabelProperties(
            enabled = true,
            labels = labels
        ),
        data = dataList,
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        minValue = minValue
    )
}

@Composable
fun PieChartTile(data: List<Double>, labels: List<String>, colors: List<Color>, modifier: Modifier = Modifier) {
    var pies = data
        .mapIndexed { index, it -> Pie(label = labels[index], data = it, color = colors[index % colors.size], selectedColor = colors[index % colors.size]) }

    PieChart(
        modifier = modifier,
        data = pies,
        onPieClick = {
            val pieIndex = pies.indexOf(it)
            pies = pies.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
        },
        selectedScale = 1.2f,
        scaleAnimEnterSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        scaleAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300),
        style = Pie.Style.Fill
    )
}