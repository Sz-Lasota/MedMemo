package com.szylas.medmemo.statistics.datastore

import co.yml.charts.common.model.Point

class TimeStatistics(
    override val title: String = "Average dosage time"
) : Statistical {
    override fun provide(): List<Point> {
        return listOf(
            Point(0f, 60*3f+25),
            Point(1f, 60*2f+35),
            Point(2f, 60*7f+36),
            Point(3f, 60*8f+47),
            Point(4f, 60*4f+25),
            Point(5f, 60*3f+27),
            Point(6f, 60*2f+25),
            Point(7f, 60*7f+26),
        )
    }
}