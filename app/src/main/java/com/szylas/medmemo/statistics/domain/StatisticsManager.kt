package com.szylas.medmemo.statistics.domain

import android.util.Log
import android.widget.Toast
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.common.domain.formatters.formatDate
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.statistics.datastore.IMemoRepository
import java.util.Calendar

class StatisticsManager(
    private val repository: IMemoRepository,
) {

    private var memos: List<Memo> = listOf()

    suspend fun update(callback: (Boolean) -> Unit) {
        val user = Session.user() ?: throw RuntimeException("Session not found!")
        Log.d("User", user)
        repository.fetchAll(
            user = user,
            onSuccess = {
                memos = it
                callback(true)
            },
            onError = {
                callback(false)
            }
        )
    }

    fun pillsStatus(time: Calendar): List<Int> {
        val lookupNotifications = memos
            .flatMap { it.notifications }
            .filter {
                it.date.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR) && it.date.get(
                    Calendar.YEAR
                ) == time.get(Calendar.YEAR)
            }

        return listOf(
            lookupNotifications.count { it.intakeTime != null },
            lookupNotifications.size
        )
    }

    fun active(): Int {
        return memos
            .filter { it.startDate.before(Calendar.getInstance()) }
            .filter { it.finishDate == null || it.finishDate!!.after(Calendar.getInstance()) }
            .size
    }

    fun names(): List<String> {
        val today = Calendar.getInstance()
        val weekBefore = Calendar.getInstance().apply { add(Calendar.DATE, -7) }
        return memos
            .filter { it.startDate.before(today) }
            .filter { it.finishDate == null || it.finishDate!!.after(today) }
            .map { it.name }
    }

    fun pillsTime(): Pair<List<List<Double>>, List<String>> {
        val therapy = memos.firstOrNull { it.name == "test2" }
        if (therapy == null) {
            return Pair(emptyList(), listOf())
        }

        Log.d("Stat_therapy_name", therapy.name)

        val notifications = therapy.notifications
            .filter { it.date.after(Calendar.getInstance().apply { add(Calendar.DATE, -7) })
                    && it.date.before(Calendar.getInstance()) }
        val hours = notifications.map { it.baseDosageTime }.distinct()

        val series: MutableList<List<Double>> = mutableListOf()
        hours.forEach {hour ->
            series.add(
                notifications
                    .filter { it.baseDosageTime == hour }
                    .map { it.date }
                    .map { if (it == null) -1.0 else it.get(Calendar.HOUR_OF_DAY) + it.get(Calendar.MINUTE) / 100.0 }
            )
        }
        series.firstOrNull()?.forEach {
            Log.d("Element", it.toString())
        }

        val xAxis = notifications
            .filter { it.baseDosageTime == hours.firstOrNull() }
            .map { formatDate(it.date) }

        return Pair(series, xAxis)
    }


}