package com.szylas.medmemo.statistics.domain

import android.util.Log
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.common.domain.formatters.formatDate
import com.szylas.medmemo.common.domain.formatters.formatFullDate
import com.szylas.medmemo.common.domain.formatters.formatTime
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.statistics.datastore.IMemoRepository
import java.util.Calendar

class StatisticsManager(
    private val repository: IMemoRepository?,
) {

    var memos: List<Memo> = listOf()

    suspend fun update(callback: (Boolean) -> Unit) {
        val user = Session.user() ?: throw RuntimeException("Session not found!")
        Log.d("User", user)
        repository!!.fetchAll(
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

        return memos
            .filter { it.startDate.before(today) }
            .filter { it.finishDate == null || it.finishDate!!.after(today) }
            .map { it.name }
    }

    fun pillsTime(
        name: String,
        startDate: Calendar = Calendar.getInstance(),
    ): Pair<List<List<Double>>, List<List<String>>> {
        val therapy = memos.firstOrNull { it.name == name }
        if (therapy == null) {
            return Pair(emptyList(), listOf())
        }


        val notifications = therapy.notifications
            .filter {
                it.date.after(Calendar.getInstance().apply {
                    timeInMillis = startDate.timeInMillis
                    add(Calendar.DATE, -5)
                    set(Calendar.HOUR_OF_DAY, 1)
                })
                        && it.date.before(Calendar.getInstance().apply {
                    timeInMillis = startDate.timeInMillis
                    add(Calendar.DATE, 1)
                    set(Calendar.HOUR_OF_DAY, 1)
                })
            }

        val hours = notifications.map { it.baseDosageTime }.distinct()

        val series: MutableList<List<Double>> = mutableListOf()
        hours.forEach { hour ->
            series.add(
                notifications
                    .filter { it.baseDosageTime == hour }
                    .map { it.intakeTime }
                    .map {
                        if (it == null)
                            hour.toDouble()
                        else
                            it.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE).toDouble()
                    }
            )
        }

        val xAxis = notifications
            .filter { it.baseDosageTime == hours.firstOrNull() }
            .map { formatDate(it.date) }

        val titles = hours
            .map { formatTime(it) }

        return Pair(series, listOf(xAxis, titles))
    }

    fun missed(): Pair<Double, Double> {
        val today = Calendar.getInstance()
        val currentMemos = memos
            .filter { it.startDate.before(today) }
            .filter { it.finishDate == null || it.finishDate!!.after(today) }

        val currentNotifications = currentMemos
            .flatMap { it.notifications }
            .filter {
                it.date.after(Calendar.getInstance().apply {
                    add(Calendar.DATE, -5)
                    set(Calendar.HOUR_OF_DAY, 1)
                }) && it.date.before(Calendar.getInstance().apply {
                    add(Calendar.DATE, 1)
                    set(Calendar.HOUR_OF_DAY, 1)
                })
            }

        return Pair(
            currentNotifications.count { it.intakeTime == null }.toDouble(),
            currentNotifications.count { it.intakeTime != null }.toDouble(),
        )
    }


}