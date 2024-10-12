package com.szylas.medmemo.memo.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.components.TextInput
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.common.presentation.theme.imageBackground
import com.szylas.medmemo.memo.datastore.PillCountFirebaseRepository
import com.szylas.medmemo.memo.datastore.models.PillCount
import com.szylas.medmemo.memo.domain.extensions.color
import com.szylas.medmemo.memo.domain.extensions.name
import com.szylas.medmemo.memo.domain.managers.PillAmountManager
import com.szylas.medmemo.memo.presentation.components.DropDownMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PillAmountActivity : ComponentActivity() {

    private val pillManager = PillAmountManager(PillCountFirebaseRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var fetched by remember {
                mutableStateOf(false)
            }
            var isDialogOpen by remember {
                mutableStateOf(false)
            }

            lifecycleScope.cleanup(
                onSuccess = {
                    lifecycleScope.fetchAll(
                        onSuccess = {
                            fetched = true
                        },
                        onError = {
                            Toast.makeText(this, "Unable to fetch from server", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                },
                onError = {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            )
            MedMemoTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = { isDialogOpen = true }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .paint(
                                imageBackground(),
                                contentScale = ContentScale.FillBounds
                            )
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (!fetched) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(64.dp),
                                )
                            }
                        } else {
                            LazyPillAmount(
                                setFetch = { fetched = it },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                            )
                        }
                        if (isDialogOpen) {
                            NewPillAmountDialog(onDismiss = { isDialogOpen = false }) {
                                lifecycleScope.update(
                                    count = it,
                                    onSuccess = {
                                        fetched = false
                                        lifecycleScope.fetchAll(
                                            onSuccess = {
                                                fetched = true
                                            },
                                            onError = {
                                                Toast.makeText(
                                                    this@PillAmountActivity,
                                                    "Unable to fetch from server",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        )
                                        Toast.makeText(
                                            this@PillAmountActivity,
                                            "Successfully created med count",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            this@PillAmountActivity,
                                            it,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                                isDialogOpen = false
                            }
                        }
                    }

                }
            }
        }
    }

    @Composable
    private fun LazyPillAmount(setFetch: (Boolean) -> Unit, modifier: Modifier = Modifier) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pillManager.counts) {
                PillAmountItem(
                    pillCount = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(10.dp),
                    setFetch = setFetch
                )
            }

            if (pillManager.counts.isEmpty()) {
                item {
                    Text(
                        text = getString(R.string.there_are_no_active_pillcounts),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @Composable
    private fun PillAmountItem(
        pillCount: PillCount,
        modifier: Modifier = Modifier,
        setFetch: (Boolean) -> Unit,
    ) {
        var isDialogOpen by remember {
            mutableStateOf(false)
        }

        Column(
            modifier = modifier
        ) {
            Text(text = pillCount.name(), style = MaterialTheme.typography.headlineMedium)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { isDialogOpen = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (pillCount.count != pillCount.maxAmount && pillCount.count != 0) {
                        Row(
                            modifier = Modifier
                                .weight(pillCount.count / 100f)
                                .background(pillCount.color())
                                .padding(10.dp)
                        ) { Text(text = "", style = MaterialTheme.typography.headlineSmall) }
                        Row(
                            modifier = Modifier
                                .weight((pillCount.maxAmount - pillCount.count) / 100f)
                                .padding(10.dp)
                        ) { Text(text = "", style = MaterialTheme.typography.headlineSmall) }
                    } else if (pillCount.count == pillCount.maxAmount) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(pillCount.color())
                                .padding(10.dp)
                        ) { Text(text = "", style = MaterialTheme.typography.headlineSmall) }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) { Text(text = "", style = MaterialTheme.typography.headlineSmall) }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Pills: ${pillCount.count}/${pillCount.maxAmount}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Refill", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        if (isDialogOpen) {
            RefillDialog(
                initCount = pillCount.count,
                onConfirm = {
                    pillCount.count = it
                    pillCount.maxAmount = it
                    lifecycleScope.update(
                        count = pillCount,
                        onSuccess = {
                            setFetch(false)
                            lifecycleScope.fetchAll(
                                onSuccess = {
                                    setFetch(true)
                                },
                                onError = {
                                    Toast.makeText(
                                        this@PillAmountActivity,
                                        "Unable to fetch from server",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            )
                            Toast.makeText(
                                this@PillAmountActivity,
                                "Successfully created med count",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = {
                            Toast.makeText(
                                this@PillAmountActivity,
                                "Successfully created med count",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    isDialogOpen = false
                },
                onDismiss = {
                    isDialogOpen = false
                })
        }
    }

    @Composable
    private fun RefillDialog(
        initCount: Int,
        onConfirm: (Int) -> Unit,
        onDismiss: () -> Unit,
    ) {
        var count by remember {
            mutableStateOf("$initCount")
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
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = { onConfirm(count.toInt()) }) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }

    @Composable
    private fun NewPillAmountDialog(onDismiss: () -> Unit, onConfirm: (PillCount) -> Unit) {
        var count by remember {
            mutableStateOf(PillCount())
        }
        var amount by remember {
            mutableStateOf("")
        }

        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create_new_pill_tracker),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = stringResource(R.string.choose_therapy),
                    style = MaterialTheme.typography.titleLarge
                )

                DropDownMenu(
                    suggestions = pillManager.active,
                    label = stringResource(R.string.therapy),
                    modifier = Modifier,
                    onValueChange = {
                        count.id = it
                    })
                Text(
                    text = stringResource(R.string.enter_amount_of_pills),
                    style = MaterialTheme.typography.titleLarge
                )
                TextInput(
                    value = "$amount",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = stringResource(R.string.pills_amount),
                    onValueChange = {
                        if (it.isBlank()) {
                            amount = ""
                            return@TextInput
                        }
                        if (!it.isDigitsOnly()) {
                            return@TextInput
                        }
                        amount = it
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = {
                        if (amount.isBlank() || amount.toInt() == 0) {
                            Toast.makeText(
                                this@PillAmountActivity,
                                "Entered amount is blank or less than 0",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@TextButton
                        }
                        count.maxAmount = amount.toInt()
                        count.count = amount.toInt()

                        onConfirm(count)
                    }) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }

    private fun CoroutineScope.update(
        count: PillCount,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) = launch {
        pillManager.update(count, onSuccess, onError)
    }

    private fun CoroutineScope.cleanup(onSuccess: () -> Unit, onError: (String) -> Unit) = launch {
        pillManager.cleanup(onSuccess, onError)
    }

    private fun CoroutineScope.fetchAll(onSuccess: () -> Unit, onError: (String) -> Unit) = launch {
        pillManager.fetchAll(onSuccess, onError)
    }

}