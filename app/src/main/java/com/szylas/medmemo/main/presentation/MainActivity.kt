package com.szylas.medmemo.main.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.szylas.medmemo.R
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.main.presentation.models.CalendarScreen
import com.szylas.medmemo.main.presentation.models.HomeScreen
import com.szylas.medmemo.main.presentation.models.NavBarItem
import com.szylas.medmemo.common.presentation.style.NavBarItemStyleProvider
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme
import com.szylas.medmemo.main.presentation.models.StatisticsScreen
import com.szylas.medmemo.main.presentation.views.CalendarFragment
import com.szylas.medmemo.main.presentation.views.HomeFragment
import com.szylas.medmemo.main.presentation.views.ProfileFragment
import com.szylas.medmemo.memo.domain.managers.MemoManagerProvider
import com.szylas.medmemo.memo.domain.notifications.MemoNotificationReceiver
import com.szylas.medmemo.memo.domain.notifications.registerNotificationChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


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


    @OptIn(ExperimentalMaterial3Api::class)
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


                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                        )

                    })
                }, bottomBar = {
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
                }) { innerPadding ->
                    registerNotificationChannel(this)
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        NavHost(navController = navController, startDestination = HomeScreen) {
                            composable<HomeScreen> {
                                HomeFragment(activity = this@MainActivity)
                            }
                            composable<CalendarScreen> {
                                CalendarFragment(activity = this@MainActivity)
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

    private fun scheduleNotifications(notifications: Map<Memo, List<MemoNotification>>) {
        notifications.forEach { (memo, notificationList) ->
            notificationList.forEach {
                val intent =
                    Intent(applicationContext, MemoNotificationReceiver::class.java).apply {
                        putExtra("NOTIFICATION", it)
                        putExtra("MEMO", memo)
                    }

                val pendingIntent = PendingIntent.getBroadcast(
                    applicationContext,
                    it.notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                Log.d(
                    "SCHEDULE",
                    "Notification (id: ${it.notificationId}, name: ${it.name}) scheduled at: ${it.date.timeInMillis}"
                )

                alarm.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, it.date.timeInMillis, pendingIntent
                )
            }
        }
    }


    private fun CoroutineScope.updateEndless(
        onSuccess: (Map<Memo, List<MemoNotification>>) -> Unit,
        onError: (String) -> Unit,
        onSessionNotFound: () -> Unit
    ) = launch {
        MemoManagerProvider.memoManager.updateEndless(onSuccess, onError, onSessionNotFound)
    }

}

