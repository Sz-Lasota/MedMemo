package com.szylas.medmemo.memo.presentation.views

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.formatters.formatFullDate
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memo.presentation.components.StatusBarManager
import com.szylas.medmemo.memo.presentation.components.TooltipModal
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDateFragment(
    activity: ComponentActivity,
    statusBarManager: StatusBarManager,
    memo: Memo,
    navigation: () -> Unit,
) {

    var isStartTooltip by remember {
        mutableStateOf(false)
    }

    var isFinishTooltip by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        var startDate by remember {
            mutableStateOf(memo.startDate)
        }

        var finishDate: Calendar? by remember {
            mutableStateOf(memo.finishDate)
        }

        var endless by remember {
            mutableStateOf(memo.finishDate == null)
        }

        val startDatePickerState = rememberDatePickerState()
        var startShowDatePicker by remember { mutableStateOf(false) }

        val finishDatePickerState = rememberDatePickerState()
        var finishShowDatePicker by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.date_information),
                style = MaterialTheme.typography.headlineMedium,
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.start_date),
                style = MaterialTheme.typography.titleLarge,
                softWrap = true
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { isStartTooltip = true },
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.name_tooltip)
            )
        }
        Button(
            onClick = { startShowDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.choose))
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = formatFullDate(startDate),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            softWrap = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(R.string.finish_date),
                softWrap = true
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { isFinishTooltip = true },
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.name_tooltip)
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = !endless, onCheckedChange = {
                endless = !it
                if (!it) {
                    finishDate = null
                    memo.finishDate = null
                }
            })
        }

        Button(
            onClick = { finishShowDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = !endless
        ) {
            Text(stringResource(id = R.string.choose))
        }


        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = finishDate?.let { formatFullDate(it) } ?: activity.getString(R.string.endless),
            style = MaterialTheme.typography.labelLarge,
            softWrap = true,
        )


        Spacer(modifier = Modifier.weight(1f))
        statusBarManager.StatusBar()
        Spacer(modifier = Modifier.weight(0.2f))
        Button(
            onClick = navigation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.next))
        }

        if (isStartTooltip) {
            TooltipModal(title = stringResource(id = R.string.start_date), body = stringResource(R.string.start_tooltip)) {
                isStartTooltip = false
            }
        }

        if (isFinishTooltip) {
            TooltipModal(title = stringResource(R.string.finish_date_), body = stringResource(R.string.finish_tooltip)) {
                isFinishTooltip = false
            }
        }

        if (startShowDatePicker) {
            DatePickerDialog(onDismissRequest = { startShowDatePicker = false }, confirmButton = {
                TextButton(onClick = {
                    val selectedDate = Calendar.getInstance().apply {
                        timeInMillis = startDatePickerState.selectedDateMillis!!
                    }
                    if (selectedDate.after(Calendar.getInstance()) && (finishDate == null || selectedDate.before(
                            finishDate
                        ))
                    ) {
                        startDate = selectedDate
                        memo.startDate = startDate

                    } else {
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.before_today),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    startShowDatePicker = false
                }) {
                    Text(text = stringResource(R.string.ok), fontSize = 18.sp)
                }
            }, dismissButton = {
                TextButton(onClick = {
                    startShowDatePicker = false
                }) { Text(text = stringResource(R.string.cancel), fontSize = 18.sp) }
            }) {
                DatePicker(state = startDatePickerState)
            }
        }

        if (finishShowDatePicker) {
            DatePickerDialog(onDismissRequest = { finishShowDatePicker = false }, confirmButton = {
                TextButton(onClick = {
                    val selectedDate = Calendar.getInstance().apply {
                        timeInMillis = finishDatePickerState.selectedDateMillis!!
                    }
                    if (selectedDate.after(Calendar.getInstance()) && selectedDate.after(
                            startDate
                        )
                    ) {
                        finishDate = selectedDate
                        memo.finishDate = finishDate
                    } else {
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.before_start),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finishShowDatePicker = false
                }) {
                    Text(text = stringResource(R.string.ok), fontSize = 18.sp)
                }
            }, dismissButton = {
                TextButton(onClick = {
                    finishShowDatePicker = false
                }) { Text(text = stringResource(R.string.cancel), fontSize = 18.sp) }
            }) {
                DatePicker(state = finishDatePickerState)
            }
        }
    }
}