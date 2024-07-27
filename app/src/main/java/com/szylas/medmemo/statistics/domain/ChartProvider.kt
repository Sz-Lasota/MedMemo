package com.szylas.medmemo.statistics.domain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.szylas.medmemo.statistics.datastore.Statistical

object ChartProvider {

    fun provide(statistics: Statistical): @Composable () -> Unit {
        val data = statistics.provide()
        val xAxis = getXAxis(data)
        val yAxis = getYAxis(data)

        val plotData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = data,
                        LineStyle(),
                        IntersectionPoint(),
                        SelectionHighlightPoint(),
                        ShadowUnderLine(),
                        SelectionHighlightPopUp()
                    )
                )
            ),
            isZoomAllowed = false,
            containerPaddingEnd = 0.dp,
            paddingRight = 0.dp,
            backgroundColor = Color.White,
            xAxisData = xAxis,
            yAxisData = yAxis,
        )

        return {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalArrangement = Arrangement.Center
            ) {
                LineChart(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(250.dp),
                    lineChartData = plotData
                )

            }
        }
    }

    private fun getXAxis(data: List<Point>): AxisData {
        return AxisData.Builder()
            .steps(data.size - 1)
            .labelData {
                data[it].x.toString()
            }
            .build()
    }

    private fun getYAxis(data: List<Point>): AxisData {
        return AxisData.Builder()
            .steps(5)
            .backgroundColor(Color.White)
//            .labelAndAxisLinePadding(20.dp)
            .labelData {
                    i ->
                // Add yMin to get the negative axis values to the scale
                val yMin = data.minOf { it.y }
                val yMax = data.maxOf { it.y }
                val yScale = (yMax - yMin) / 5
                ((i * yScale) + yMin).formatToSinglePrecision()
            }
            .build()
    }

}