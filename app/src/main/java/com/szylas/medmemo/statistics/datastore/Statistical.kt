package com.szylas.medmemo.statistics.datastore

import co.yml.charts.common.model.Point

interface Statistical {
    val title: String

    fun provide(): List<Point>
}