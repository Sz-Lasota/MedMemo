package com.szylas.medmemo.memo.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.szylas.medmemo.R
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.auth.presentation.LoginActivity
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.common.presentation.components.PrimaryButton
import com.szylas.medmemo.common.presentation.components.SecondaryButton
import com.szylas.medmemo.common.presentation.components.TimePickerDialog
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.memo.domain.extensions.getMemo
import com.szylas.medmemo.memo.domain.extensions.getNotification
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.notifications.NotificationsScheduler
import com.szylas.medmemo.memo.domain.notifications.registerNotificationChannel
import com.szylas.medmemo.memo.domain.predictions.IPrediction
import com.szylas.medmemo.memo.domain.predictions.WeightedAveragePrediction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

class MemoTakenActivity : ComponentActivity() {

    private val memoManager = MemoManagerProvider.memoManager
    private val notificationsScheduler = NotificationsScheduler(this)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerNotificationChannel(this)
        enableEdgeToEdge(
        )

        val user = Session.user()
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val memo = intent.getMemo()
        val notification = intent.getNotification()

        setContent {
            MedMemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                        )

                    })
                }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val timePickerState = rememberTimePickerState()
                        var showTimePicker by remember { mutableStateOf(false) }
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = memo.name,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                style = TextStyleProvider.provide(
                                    style = TextStyleOption.TITLE_LARGE
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            PrimaryButton(text = "Med taken now", onClick = {
                                val indexOfNotification =
                                    memo.notifications.indexOf(memo.notifications.firstOrNull { it.notificationId == notification.notificationId })
                                memo.notifications[indexOfNotification].intakeTime =
                                    Calendar.getInstance()

                                lifecycleScope.rescheduleNotification(
                                    memo,
                                    notification,
                                    WeightedAveragePrediction(),
                                    onSuccess = {
                                        finish()
                                    }, onError = {
                                        Toast.makeText(
                                            this@MemoTakenActivity,
                                            "Unable to save intake time: $it! Check your internet connection or try again later!",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }, onSessionNotFound = {
                                        Log.e("SESSION", "Session not found")
                                    }
                                )

                                Log.d("MED", "Taken now ${notification.date}")
                            }, modifier = Modifier.fillMaxWidth())
                            PrimaryButton(
                                text = "Taken at",
                                onClick = { showTimePicker = true },
                                modifier = Modifier.fillMaxWidth()
                            )
                            SecondaryButton(text = "Pass", onClick = {
                                finish()
                            }, modifier = Modifier.fillMaxWidth())
                        }

                        if (showTimePicker) {
                            TimePickerDialog(
                                onDismissRequest = { showTimePicker = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        val indexOfNotification = memo.notifications
                                            .indexOf(memo.notifications
                                                .firstOrNull { it.notificationId == notification.notificationId }
                                            )

                                        memo.notifications[indexOfNotification].intakeTime =
                                            Calendar.getInstance().apply {
                                                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                                set(Calendar.MINUTE, timePickerState.minute)
                                            }

                                        lifecycleScope.rescheduleNotification(
                                            memo,
                                            notification,
                                            WeightedAveragePrediction(),
                                            onSuccess = {
                                                finish()
                                            },
                                            onError = {
                                                Toast.makeText(
                                                    this@MemoTakenActivity,
                                                    "Unable to save intake time: $it! Check your internet connection or try again later!",
                                                    Toast.LENGTH_SHORT,
                                                ).show()
                                            },
                                            onSessionNotFound = {
                                                Log.e("SESSION", "Session not found")
                                            }
                                        )

                                        showTimePicker = false
                                    }) {
                                        Text(
                                            text = stringResource(
                                                R.string.ok
                                            ), fontSize = 18.sp
                                        )
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        showTimePicker = false
                                    }) {
                                        Text(
                                            text = stringResource(R.string.cancel), fontSize = 18.sp
                                        )
                                    }
                                }
                            ) {
                                TimePicker(state = timePickerState)
                            }

                        }
                    }
                }
            }
        }
    }

    private fun CoroutineScope.rescheduleNotification(
        memo: Memo,
        lastNotification: MemoNotification,
        prediction: IPrediction,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit
    ) = launch {
        notificationsScheduler.rescheduleNotifications(
            memo,
            lastNotification,
            prediction,
            onSuccess,
            onError,
            onSessionNotFound
        )
    }

    private fun CoroutineScope.updateMemo(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit
    ) = launch {
        memoManager.updateMemo(memo, onSuccess, onError, onSessionNotFound)
    }
}