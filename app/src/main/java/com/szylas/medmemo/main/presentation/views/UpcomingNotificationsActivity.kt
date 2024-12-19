package com.szylas.medmemo.main.presentation.views

import android.content.Intent
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.MutableState
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
import com.szylas.medmemo.main.domain.getUpcomingNotifications
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.notifications.NotificationsScheduler
import com.szylas.medmemo.memo.presentation.MemoTakenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpcomingNotificationsActivity : ComponentActivity() {

    private val memoManager = MemoManagerProvider.memoManager
    private var memos: MutableState<List<Memo>> = mutableStateOf(listOf())
    private var error: MutableState<String> = mutableStateOf("")
    private var loaded: MutableState<Boolean> = mutableStateOf(false)

    override fun onResume() {
        loaded.value = false
        super.onResume()
        lifecycleScope.loadActive(
            onSuccess = {
                memos.value = it
                loaded.value = true
            }, onError = {
                error.value = it
                memos.value = listOf()
                loaded.value = true
            },
            onSessionNotFound = {
                error.value = "Session not found, log in and try again!"
                loaded.value = true
            })
    }

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


                    lifecycleScope.loadActive(
                        onSuccess = {
                            memos.value = it
                            loaded.value = true
                        }, onError = {
                            error.value = it
                            memos.value = listOf()
                            loaded.value = true
                        },
                        onSessionNotFound = {
                            error.value = "Session not found, log in and try again!"
                            loaded.value = true
                        })

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        if (!loaded.value) {
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
                        } else if (error.value.isNotBlank()) {
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
                            val notifications by remember { mutableStateOf(getUpcomingNotifications(memos.value)) }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (notifications.isEmpty()) {
                                    item {
                                        Text(
                                            modifier = Modifier.fillMaxSize(),
                                            textAlign = TextAlign.Center,
                                            text = stringResource(R.string.there_are_no_upcoming_notifications),
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }
                                }
                                items(notifications) { (memo, notification) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.at, notification.name, formatTime(notification.date)),
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Button(onClick = {
                                            startActivity(Intent(this@UpcomingNotificationsActivity, MemoTakenActivity::class.java).apply {
                                                putExtra("MEMO", memo)
                                                putExtra("NOTIFICATION", notification)
                                            })
                                        }) {
                                            Text(
                                                text = stringResource(R.string.take),
                                                maxLines = 1,
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
    }

    private fun CoroutineScope.loadActive(
        onSuccess: (List<Memo>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit,
    ) = launch {
        MemoManagerProvider.memoManager.loadActive(onSuccess, onError, onSessionNotFound)
    }
}