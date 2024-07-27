package com.szylas.medmemo.statistics.datastore

import co.yml.charts.common.model.Point

class MissedStatistics(
    override val title: String = "Missed doses"
) : Statistical {
    override fun provide(): List<Point> {
        return listOf(
            Point(0f, 3f),
            Point(1f, 2f),
            Point(2f, 7f),
            Point(3f, 8f),
            Point(4f, 4f),
            Point(5f, 3f),
            Point(6f, 2f),
            Point(7f, 7f),
            Point(8f, 7f),
            Point(9f, 7f),
            Point(10f, 7f),
            Point(11f, 7f),
            Point(12f, 7f),
            Point(13f, 7f),
            Point(14f, 7f),
        )
    }

}