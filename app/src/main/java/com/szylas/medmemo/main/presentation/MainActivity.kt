package com.szylas.medmemo.main.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.common.presentation.theme.imageBackground
import com.szylas.medmemo.main.presentation.models.CalendarScreen
import com.szylas.medmemo.main.presentation.models.HomeScreen
import com.szylas.medmemo.main.presentation.models.NavBarItem
import com.szylas.medmemo.main.presentation.models.StatisticsScreen
import com.szylas.medmemo.main.presentation.views.CalendarFragment
import com.szylas.medmemo.main.presentation.views.HomeFragment
import com.szylas.medmemo.main.presentation.views.ProfileFragment
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.notifications.MemoNotificationReceiver
import com.szylas.medmemo.memo.domain.notifications.NotificationsScheduler
import com.szylas.medmemo.memo.domain.notifications.registerNotificationChannel
import com.szylas.medmemo.memo.presentation.NewMemoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val notificationsScheduler = NotificationsScheduler(this)

    private val bottomNavItems = listOf(
        NavBarItem(
            destination = HomeScreen,
            label = "Home",
            selectedIcon = R.drawable.home_filled,
            unselectedIcon = R.drawable.home_outlined,
        ),
        NavBarItem(
            destination = CalendarScreen,
            label = "Calendar",
            selectedIcon = R.drawable.calendar_filled,
            unselectedIcon = R.drawable.calendar_outlined,
        ),
        NavBarItem(
            destination = StatisticsScreen,
            label = "Stats",
            selectedIcon = R.drawable.user_filled,
            unselectedIcon = R.drawable.user_outlined,
        ),
    )

    private var memos: MutableState<List<Memo>?> = mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        lifecycleScope.updateEndless(onSuccess = this::scheduleNotifications, onError = {
            Toast.makeText(this, "Error when updating memos $it!", Toast.LENGTH_SHORT).show()
        }, onSessionNotFound = { finish() })



        setContent {
            MedMemoTheme {
                val navController = rememberNavController()

                var selectedBottomIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                // Loading active memos
                lifecycleScope.loadActive(
                    onSuccess = { memos.value = it },
                    onError = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() },
                    onSessionNotFound = {
                        Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                )

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                        ) {
                            bottomNavItems.fastForEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedBottomIndex == index,
                                    onClick = {
                                        selectedBottomIndex = index
                                        navController.navigate(item.destination)
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = if (selectedBottomIndex == index) item.selectedIcon else item.unselectedIcon),
                                            contentDescription = item.label
                                        )
                                    })
                            }
                        }
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.surface,
                            onClick = {
                                startActivity(
                                    Intent(
                                        this,
                                        NewMemoActivity::class.java
                                    )
                                )
                            }) {
                            Text(text = stringResource(id = R.string.new_memo))
                        }
                    }
                ) { innerPadding ->
                    registerNotificationChannel(this)
                    Box(
                        modifier = Modifier
                            .paint(
                                imageBackground(),
                                contentScale = ContentScale.FillBounds
                            )
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        NavHost(navController = navController, startDestination = HomeScreen) {
                            composable<HomeScreen> {
                                HomeFragment(memos = memos.value, activity = this@MainActivity)
                            }
                            composable<CalendarScreen> {
                                CalendarFragment(memos = memos.value, activity = this@MainActivity)
                            }
                            composable<StatisticsScreen> {
                                ProfileFragment(activity = this@MainActivity)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Loading active memos
        lifecycleScope.loadActive(
            onSuccess = { memos.value = it },
            onError = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() },
            onSessionNotFound = {
                Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show()
                finish()
            }
        )

    }

    private fun scheduleNotifications(notifications: Map<Memo, List<MemoNotification>>) {
        notifications.forEach { (memo, notificationList) ->
            notificationList.forEach {
                notificationsScheduler.scheduleNotification(memo, it)

                Log.d(
                    "SCHEDULE",
                    "Notification (id: ${it.notificationId}, name: ${it.name}) scheduled at: ${it.date.timeInMillis}"
                )
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

    private fun CoroutineScope.updateEndless(
        onSuccess: (Map<Memo, List<MemoNotification>>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit,
    ) = launch {
        MemoManagerProvider.memoManager.updateEndless(onSuccess, onError, onSessionNotFound)
    }

}

