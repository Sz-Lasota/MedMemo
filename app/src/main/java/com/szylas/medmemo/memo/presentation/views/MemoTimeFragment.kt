package com.szylas.medmemo.memo.presentation.views

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.formatters.timeString
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.components.TimePickerDialog
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memo.domain.extensions.provideTimes
import com.szylas.medmemo.memo.presentation.components.StatusBarManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoTimeFragment(
    activity: ComponentActivity,
    statusBarManager: StatusBarManager,
    memo: Memo,
    navigation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        var smartMode by remember {
            mutableStateOf(memo.smartMode)
        }

        var hours: List<Int> by remember {
            mutableStateOf(memo.provideTimes())
        }

        val timePickerState = rememberTimePickerState()
        var showTimePicker by remember { mutableStateOf(false) }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.time_information),
                style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE),
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.reminder_mode),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM),
            softWrap = true
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.strict_mode), fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = smartMode, onCheckedChange = {
                smartMode = it
                memo.smartMode = it
            })
            Spacer(modifier = Modifier.weight(1f))
            Text(text = stringResource(R.string.smart_mode), fontSize = 20.sp)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.hours_of_dosing),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM),
            softWrap = true
        )
        PrimaryButton(
            text = stringResource(R.string.add),
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(hours) {
                ListItem(
                    title = timeString(it),
                    onEdit = { newHour ->
                        if (hours.contains(newHour)) {
                            Toast.makeText(
                                activity, "This hour of dosage already exists", Toast.LENGTH_SHORT
                            ).show()
                            return@ListItem
                        }
                        hours = mutableListOf<Int>().also { list ->
                            list.addAll(hours)
                            list.add(newHour)
                            list.remove(it)
                        }.toList()
                        memo.dosageTime = hours
                        Log.e("MEMO TAG", "$it")
                    },
                    onRemove = {
                        hours.forEachIndexed { index, item ->
                            Log.d("MEMO hour: $index", "$item")
                        }
                        Log.e("PLACEHOLDER", "PLACEHOLDER")
                        hours = mutableListOf<Int>().also { list ->
                            list.addAll(hours)
                            list.remove(it)
                        }.toList()
                        memo.dosageTime = hours
                        hours.forEachIndexed { index, item ->
                            Log.d("MEMO hour: $index", "$item")
                        }
                        Log.e("MEMO TAG", "$it")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        statusBarManager.StatusBar()
        Spacer(modifier = Modifier.weight(0.2f))
        SecondaryButton(
            text = stringResource(id = R.string.next),
            onClick = navigation,
            modifier = Modifier.fillMaxWidth()
        )

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (hours.contains(timePickerState.hour * 60 + timePickerState.minute)) {
                            Toast.makeText(
                                activity, "This hour of dosage already exists", Toast.LENGTH_SHORT
                            ).show()
                            showTimePicker = false
                            return@TextButton
                        }
                        Toast.makeText(
                            activity,
                            "${timePickerState.hour * 60 + timePickerState.minute}",
                            Toast.LENGTH_SHORT
                        ).show()
                        hours =
                            mutableListOf(timePickerState.hour * 60 + timePickerState.minute).also {
                                it.addAll(hours)
                            }.toList()
                        memo.dosageTime = hours

                        showTimePicker = false
                    }) { Text(text = stringResource(R.string.ok), fontSize = 18.sp) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showTimePicker = false
                    }) { Text(text = stringResource(R.string.cancel), fontSize = 18.sp) }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    title: String, onEdit: (Int) -> Unit, onRemove: () -> Unit, modifier: Modifier = Modifier
) {
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    val timePickerState = rememberTimePickerState()

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = title, style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL))
        Spacer(modifier = Modifier.weight(1f))
        SecondaryButton(
            text = stringResource(R.string.edit), onClick = { showTimePicker = true }
        )
        PrimaryButton(
            text = stringResource(R.string.remove), onClick = onRemove
        )


        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        onEdit(timePickerState.hour * 60 + timePickerState.minute)
                        showTimePicker = false
                    }) { Text(text = stringResource(R.string.ok), fontSize = 18.sp) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showTimePicker = false
                    }) { Text(text = stringResource(R.string.cancel), fontSize = 18.sp) }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}