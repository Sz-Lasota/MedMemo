package com.szylas.medmemo.memos.presentation.views

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memos.presentation.components.StatusBarManager

@Composable
fun MemoNameFragment(
    activity: ComponentActivity, statusBarManager: StatusBarManager, navigation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        var medName by remember {
            mutableStateOf("")
        }
        var medDescription by remember {
            mutableStateOf("")
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
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.this_name_will_be_used_to_display_notifications_and_events_in_calendar),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
            color = MaterialTheme.colorScheme.outline
        )
        TextInput(modifier = Modifier.fillMaxWidth(),
            value = medName,
            label = stringResource(id = R.string.medicament_name),
            onValueChange = {
                medName = it
            })
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.dosage_information),
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM)
        )
        TextInput(modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            value = medDescription,
            label = stringResource(id = R.string.dosage_information),
            onValueChange = {
                medDescription = it
            })
        PrimaryButton(
            text = stringResource(R.string.read_my_cmi),
            onClick = { Toast.makeText(activity, "Read my cmi", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth()
        )
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