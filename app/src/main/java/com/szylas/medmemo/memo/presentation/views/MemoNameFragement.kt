package com.szylas.medmemo.memo.presentation.views

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.memo.presentation.components.StatusBarManager
import com.szylas.medmemo.memo.presentation.components.TooltipModal

@Composable
fun MemoNameFragment(
    activity: ComponentActivity,
    statusBarManager: StatusBarManager,
    memo: Memo,
    navigation: () -> Unit,
) {

    var isNameTooltip by remember {
        mutableStateOf(false)
    }
    var isDosageTooltip by remember {
        mutableStateOf(false)
    }
    var isGapTooltip by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        var medName by remember {
            mutableStateOf(memo.name)
        }
        var numberOfDoses by remember {
            mutableStateOf("")
        }

        var gapHour by remember {
            mutableStateOf("")
        }
        var gapMinute by remember {
            mutableStateOf("")
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.medicament_information),
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
                text = stringResource(R.string.medicament_name),
                style = MaterialTheme.typography.titleLarge,
                softWrap = true
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { isNameTooltip = true },
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.name_tooltip)
            )

        }
        TextInput(modifier = Modifier.fillMaxWidth(),
            value = medName,
            label = stringResource(id = R.string.medicament_name),
            onValueChange = {
                medName = it
                memo.name = it
            })
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = stringResource(R.string.number_of_doses),
                style = MaterialTheme.typography.titleLarge,
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { isDosageTooltip = true },
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.name_tooltip)
            )
        }
        TextInput(modifier = Modifier.fillMaxWidth(),
            value = numberOfDoses,
            label = stringResource(id = R.string.number_of_doses),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (!it.isDigitsOnly()) {
                    return@TextInput
                }
                numberOfDoses = it
                memo.numberOfDoses = it.toIntOrNull() ?: 0
            })
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = stringResource(R.string.gap_between_doses),
                style = MaterialTheme.typography.titleLarge,
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { isGapTooltip = true },
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.name_tooltip)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            TextInput(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = gapHour,
                label = stringResource(R.string.hours),
                onValueChange = {
                    if ((it.toIntOrNull() ?: 0) >= 24) {
                        return@TextInput
                    }
                    if (!it.isDigitsOnly()) {
                        return@TextInput
                    }
                    gapHour = it
                    memo.gap = (it.toIntOrNull() ?: 0) * 60 + (gapMinute.toIntOrNull() ?: 0)
                },
                modifier = Modifier.weight(1f)
            )
            TextInput(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = gapMinute,
                label = stringResource(R.string.minutes),
                onValueChange = {
                    if ((it.toIntOrNull() ?: 0) >= 60) {
                        return@TextInput
                    }
                    if (!it.isDigitsOnly()) {
                        return@TextInput
                    }
                    gapMinute = it
                    memo.gap = (it.toIntOrNull() ?: 0) + (gapHour.toIntOrNull() ?: 0) * 60
                },
                modifier = Modifier.weight(1f)
            )


        }

        Spacer(modifier = Modifier.weight(1f))
        statusBarManager.StatusBar()
        Spacer(modifier = Modifier.weight(0.2f))
        Button(
            onClick = {
                if (memo.name.isBlank()) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.memo_name_cannot_be_blank),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (numberOfDoses.isBlank() || memo.numberOfDoses < 1) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.there_must_be_at_least_one_dose),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (memo.gap * (memo.numberOfDoses - 1) > 23 * 60 + 59) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.cannot_schedule),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                navigation()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.next))
        }

        if (isNameTooltip) {
            TooltipModal(
                title = stringResource(R.string.medicament_name),
                body = stringResource(R.string.name_tooltip_body)
            ) {
                isNameTooltip = false
            }
        }

        if (isDosageTooltip) {
            TooltipModal(
                title = stringResource(R.string.number_of_doses),
                body = stringResource(R.string.dosage_tooltip)
            ) {
                isDosageTooltip = false
            }
        }

        if (isGapTooltip) {
            TooltipModal(title = stringResource(R.string.gap_between_doses), body = stringResource(R.string.gap_tooltip)) {
                isGapTooltip = false
            }
        }
    }
}