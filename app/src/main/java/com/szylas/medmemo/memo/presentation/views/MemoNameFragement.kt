package com.szylas.medmemo.memo.presentation.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memo.presentation.components.StatusBarManager

@Composable
fun MemoNameFragment(
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
        var medName by remember {
            mutableStateOf(memo.name)
        }
        var numberOfDoses by remember {
            mutableIntStateOf(memo.numberOfDoses)
        }

        var gapHour by remember {
            mutableIntStateOf(memo.gap / 60)
        }
        var gapMinute by remember {
            mutableIntStateOf(memo.gap % 60)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.medicament_information),
                style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE),
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.medicament_name),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM),
            softWrap = true
        )
        TextInput(modifier = Modifier.fillMaxWidth(),
            value = medName,
            label = stringResource(id = R.string.medicament_name),
            onValueChange = {
                medName = it
                memo.name = it
            })
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.number_of_doses),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM)
        )
        TextInput(modifier = Modifier.fillMaxWidth(),
            value = "$numberOfDoses",
            label = stringResource(id = R.string.number_of_doses),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                numberOfDoses = it.toIntOrNull() ?: 0
                memo.numberOfDoses = it.toIntOrNull() ?: 0
            })
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.gap_between_doses),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextInput(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = "$gapHour",
                label = stringResource(R.string.hours),
                onValueChange = {
                    gapHour = it.toIntOrNull() ?: gapHour
                    memo.gap = (it.toIntOrNull() ?: 0) * 60 + gapMinute
                },
                modifier = Modifier.weight(1f)
            )
            TextInput(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = "$gapMinute",
                label = stringResource(R.string.minutes),
                onValueChange = {
                    gapMinute = it.toIntOrNull() ?: gapMinute
                    memo.gap = (it.toIntOrNull() ?: 0) + gapHour * 60
                },
                modifier = Modifier.weight(1f)
            )


        }

        Spacer(modifier = Modifier.weight(1f))
        statusBarManager.StatusBar()
        Spacer(modifier = Modifier.weight(0.2f))
        SecondaryButton(
            text = stringResource(R.string.next),
            onClick = navigation,
            modifier = Modifier.fillMaxWidth()
        )
    }
}