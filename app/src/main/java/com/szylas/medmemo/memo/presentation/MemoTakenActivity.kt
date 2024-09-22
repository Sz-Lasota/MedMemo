package com.szylas.medmemo.memo.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.szylas.medmemo.common.presentation.components.TimePickerDialog
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.memo.datastore.PillCountFirebaseRepository
import com.szylas.medmemo.memo.domain.extensions.getMemo
import com.szylas.medmemo.memo.domain.extensions.getNotification
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.managers.PillAmountManager
import com.szylas.medmemo.memo.domain.notifications.NotificationsScheduler
import com.szylas.medmemo.memo.domain.notifications.registerNotificationChannels
import com.szylas.medmemo.memo.domain.predictions.IPrediction
import com.szylas.medmemo.memo.domain.predictions.WeightedAveragePrediction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

class MemoTakenActivity : ComponentActivity() {

    private val memoManager = MemoManagerProvider.memoManager
    private val notificationsScheduler = NotificationsScheduler(this)
    private val pillAmountManager = PillAmountManager(PillCountFirebaseRepository())


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerNotificationChannels(this)
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
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = stringResource(R.string.when_did_you_take_your, memo.name),
                                style = MaterialTheme.typography.headlineLarge
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {
                                    medTakenClick(
                                        memo,
                                        notification,
                                        Calendar.getInstance()
                                    )
                                }
                            ) {
                                Text(text = stringResource(R.string.now))
                            }

                            Button(
                                modifier =  Modifier
                                    .fillMaxWidth(),
                                onClick = { showTimePicker = true }
                            ) {
                                Text(text = stringResource(R.string.at_))
                            }

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.not_planning_to_take_your_med)
                            )

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                onClick = { finish() }
                            ) {
                                Text(text = "Pass")
                            }
                        }

                        if (showTimePicker) {
                            TimePickerDialog(
                                onDismissRequest = { showTimePicker = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            medTakenClick(
                                                memo = memo,
                                                notification = notification,
                                                time = Calendar.getInstance().apply {
                                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                                    set(Calendar.MINUTE, timePickerState.minute)
                                                })
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

    private fun medTakenClick(
        memo: Memo,
        notification: MemoNotification,
        time: Calendar,
    ) {
        Log.d("Hello", "Hello")

        val indexOfNotification = memo.notifications.indexOf(
            memo.notifications
                .firstOrNull { it.notificationId == notification.notificationId }
        )
        memo.notifications[indexOfNotification].intakeTime =
            time
        notification.intakeTime = time

        lifecycleScope.decreaseAmount(
            memo = memo,
            onSuccess = {
                Log.d("PillAmount", it)
            },
            onError = {
                Log.e("PillAmount", it)
            },
            onAlarm = {
                notificationsScheduler.scheduleLowPillNotification(memo.name)
            }
        )

        notificationsScheduler.cancelNotification(memo, notification)

        if (memo.smartMode) {
            lifecycleScope.rescheduleNotification(
                memo,
                notification,
                WeightedAveragePrediction(),
                onSuccess = {
                    Toast.makeText(
                        this@MemoTakenActivity,
                        "Intake time successfully updated!",
                        Toast.LENGTH_SHORT
                    ).show()
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
            return
        }

        lifecycleScope.updateMemo(
            memo = memo,
            notification = notification,
            onSuccess = {
                Toast.makeText(
                    this@MemoTakenActivity,
                    "Intake time successfully updated!",
                    Toast.LENGTH_SHORT
                ).show()
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
    }

    private fun CoroutineScope.decreaseAmount(
        memo: Memo,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onAlarm: () -> Unit
    ) = launch {
        pillAmountManager.decrease(memo, onSuccess, onError, onAlarm)
    }

    private fun CoroutineScope.rescheduleNotification(
        memo: Memo,
        lastNotification: MemoNotification,
        prediction: IPrediction,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit,
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
        notification: MemoNotification,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: (String) -> Unit,
    ) = launch {
        memoManager.updateMemo(memo, notification, onSuccess, onError, onSessionNotFound)
    }
}