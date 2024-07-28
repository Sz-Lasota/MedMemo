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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memos.presentation.components.StatusBarManager

@Composable
fun MemoSummaryFragment(activity: ComponentActivity, statusBarManager: StatusBarManager, navigation: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.summary),
                style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE),
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.outline
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "\$MedNamePlaceholder",
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM),
            softWrap = true
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "\$MedDescriptionPlaceholder",
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
            softWrap = true
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "\$DatePlaceholder",
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM),
            softWrap = true
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "\$TimePlaceholder",
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
            softWrap = true
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "\$ModePlaceholder",
            style = TextStyleProvider.provide(style = TextStyleOption.LABEL_SMALL),
            softWrap = true
        )
        PrimaryButton(
            text = stringResource(R.string.add_picture),
            onClick = { Toast.makeText(activity, "Add pic", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        statusBarManager.StatusBar()
        Spacer(modifier = Modifier.weight(0.2f))
        SecondaryButton(text = "Finish", onClick = navigation, modifier = Modifier.fillMaxWidth())

    }

}

