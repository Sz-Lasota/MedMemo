package com.szylas.medmemo.memo.presentation.views

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.formatters.formatFullDate
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memo.datastore.PillCountFirebaseRepository
import com.szylas.medmemo.memo.datastore.models.PillCount
import com.szylas.medmemo.memo.domain.extensions.id
import com.szylas.medmemo.memo.domain.managers.PillAmountManager
import com.szylas.medmemo.memo.presentation.components.StatusBarManager
import com.szylas.medmemo.memo.presentation.components.TooltipModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val pillCountManager = PillAmountManager(PillCountFirebaseRepository())

@Composable
fun MemoSummaryFragment(
    activity: ComponentActivity,
    statusBarManager: StatusBarManager,
    memo: Memo,
    navigation: () -> Unit,
) {

    var isPillCountTooltip by remember {
        mutableStateOf(false)
    }

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    var pillCount: PillCount? by remember {
        mutableStateOf(null)
    }


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
                style = MaterialTheme.typography.headlineMedium,
                softWrap = true
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = memo.name,
                style = MaterialTheme.typography.titleLarge,
                softWrap = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.dates),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (memo.finishDate != null) "${formatFullDate(memo.startDate)} till ${
                        formatFullDate(
                            memo.finishDate!!
                        )
                    }" else "From ${formatFullDate(memo.startDate)}",
                    style = MaterialTheme.typography.labelLarge,
                    softWrap = true
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.dosage),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (memo.dosageTime.size == 1) "Once a day" else "${memo.dosageTime.size} times a day",
                    style = MaterialTheme.typography.labelLarge,
                    softWrap = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.mode),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (memo.smartMode) "Smart mode" else "Strict mode",
                    style = MaterialTheme.typography.labelLarge,
                    softWrap = true
                )
            }

        }

        Text(
            text = stringResource(R.string.optionally),
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(R.string.pill_counter),
                softWrap = true
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { isPillCountTooltip = true },
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.name_tooltip)
            )
        }

        Button(
            onClick = { isDialogOpen = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.add_pill_counter))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (pillCount == null) {
                Text(
                    text = stringResource(R.string.not_set),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.initial_amount),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${pillCount!!.count}")
                }
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        statusBarManager.StatusBar()
        Spacer(modifier = Modifier.weight(0.2f))
        Button(
            onClick = {
                if (pillCount != null) {
                    activity.lifecycleScope.update(
                        pillCount = pillCount!!,
                        onSuccess = {
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.successfully_add_pill_count),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = {
                            Toast.makeText(
                                activity,
                                it,
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                }
                navigation()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.finish))
        }

        if (isPillCountTooltip) {
            TooltipModal(title = stringResource(id = R.string.pill_counter), body = stringResource(R.string.amount_tooltip)) {
                isPillCountTooltip = false
            }
        }

        if (isDialogOpen) {
            PillCountDialog(
                init = pillCount?.count,
                activity = activity,
                onConfirm = {
                    pillCount = it
                    pillCount!!.id = memo.id()
                    isDialogOpen = false
                },
                onDelete = {
                    pillCount = null
                    isDialogOpen = false
                }) {
                isDialogOpen = false
            }
        }
    }


}

private fun CoroutineScope.update(
    pillCount: PillCount,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
) = launch {
    pillCountManager.update(pillCount, onSuccess, onError)
}


@Composable
private fun PillCountDialog(
    init: Int?,
    activity: ComponentActivity,
    onConfirm: (PillCount) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    var count by remember {
        mutableStateOf(init?.let { "$init" } ?: "")
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.enter_new_amount_of_pills),
                style = MaterialTheme.typography.headlineLarge
            )
            TextInput(
                value = "$count",
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = stringResource(R.string.pills_amount),
                onValueChange = {
                    if (it.isBlank()) {
                        count = ""
                        return@TextInput
                    }
                    if (!it.isDigitsOnly()) {
                        return@TextInput
                    }
                    count = it
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text(text = stringResource(R.string.delete))
                }
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                TextButton(onClick = {
                    if (count.isBlank() || count.toInt() <= 1) {
                        Toast.makeText(
                            activity,
                            "Pill amount should be greater than one!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }
                    onConfirm(
                        PillCount(
                            count = count.toInt(),
                            maxAmount = count.toInt()
                        )
                    )
                }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        }
    }


}
