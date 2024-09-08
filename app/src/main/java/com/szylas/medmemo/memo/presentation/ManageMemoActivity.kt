package com.szylas.medmemo.memo.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.formatters.formatFullDate
import com.szylas.medmemo.common.domain.formatters.formatTime
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.notifications.NotificationsScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// TODO:Fix this issues:
//  1. Prolong memos and schedule new reminders!
//  2. When deleting memos, cancel pending notifications


class ManageMemoActivity : ComponentActivity() {

    private val memoManager = MemoManagerProvider.memoManager
    private val notificationsScheduler = NotificationsScheduler(this)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge(
        )

        setContent {
            MedMemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                        )

                    }, navigationIcon = {
                        IconButton(onClick = {
                            finish()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, contentDescription = "Menu"
                            )
                        }
                    })
                }) { innerPadding ->
                    var memos: List<Memo> by remember {
                        mutableStateOf(listOf())
                    }
                    var loaded by remember {
                        mutableStateOf(false)
                    }
                    var error by remember {
                        mutableStateOf("")
                    }

                    var currentExpanded: Int? by remember {
                        mutableStateOf(null)
                    }
                    lifecycleScope.loadActive(
                        onSuccess = {
                            memos = it
                            loaded = true
                        }, onError = {
                            error = it
                            loaded = true
                        },
                        onSessionNotFound = {
                            error = "Session not found, log in and try again!"
                            loaded = true
                        })

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        if (!loaded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(64.dp),
                                )
                            }
                        } else if (error.isNotBlank()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Error occurred: $error",
                                    style = TextStyleProvider.provide(
                                        style = TextStyleOption.TITLE_LARGE
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                itemsIndexed(memos) { index, item ->
                                    if (currentExpanded != null && index == currentExpanded) {
                                        ExpandedMemoItem(
                                            memo = item,
                                            onFold = { currentExpanded = null },
                                            onProlongClick = { Log.d("Prolong", it.name) },
                                            onDeleteClick = { memo ->
                                                lifecycleScope.remove(
                                                    memo = memo,
                                                    onSuccess = {
                                                        memos = mutableListOf<Memo>().also { list ->
                                                            list.addAll(memos)
                                                            list.remove(memo)
                                                        }.toList()
                                                        Toast.makeText(
                                                            this@ManageMemoActivity,
                                                            it,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        currentExpanded = null
                                                    }, onError = {
                                                        Toast.makeText(
                                                            this@ManageMemoActivity,
                                                            it,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }, onSessionNotFound = {
                                                        Toast.makeText(
                                                            this@ManageMemoActivity,
                                                            "Session not found, log in and try again!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    })
                                            }
                                        )
                                    } else {
                                        FoldedMemoItem(
                                            memo = item,
                                            onClick = { currentExpanded = index },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun ExpandedMemoItem(
        memo: Memo,
        onFold: () -> Unit,
        onProlongClick: (Memo) -> Unit,
        onDeleteClick: (Memo) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memo.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onFold) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = stringResource(
                            R.string.fold
                        ),
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(15.dp)
            ) {
                Text(
                    text = "Reminder mode:",
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (memo.smartMode) "Smart" else "Strict",
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(15.dp)
            ) {
                Text(
                    text = "Dosage hours:",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = memo.dosageTime.joinToString(separator = ", ") { formatTime(it) }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(15.dp)
            ) {
                Text(
                    text = "Date range:",
                    style = MaterialTheme.typography.labelLarge,
                )

                Text(
                    text = if (memo.finishDate != null) "${formatFullDate(memo.startDate)} till ${
                        formatFullDate(
                            memo.finishDate!!
                        )
                    }" else "From ${formatFullDate(memo.startDate)}",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp
                )

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onDeleteClick(memo) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.remove))
                }

                if (memo.finishDate != null) {
                    Button(
                        onClick = { onProlongClick(memo) },
                        modifier = modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.prolong))
                    }
                }
            }

        }
    }

    @Composable
    fun FoldedMemoItem(memo: Memo, onClick: () -> Unit, modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = memo.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (memo.finishDate != null) "${formatFullDate(memo.startDate)} till ${
                        formatFullDate(
                            memo.finishDate!!
                        )
                    }" else "From ${formatFullDate(memo.startDate)}",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClick, modifier = Modifier.fillMaxHeight()) {
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "Expand")
            }
        }
    }


    private fun CoroutineScope.remove(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit,
    ) = launch {
        memoManager.deleteMemo(memo, onSuccess, onError, onSessionNotFound)
        notificationsScheduler.cancelNotifications(memo)
    }

    private fun CoroutineScope.loadActive(
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit,
    ) = launch {
        memoManager.loadActive(onSuccess, onError, onSessionNotFound)
    }
}