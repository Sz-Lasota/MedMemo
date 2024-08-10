package com.szylas.medmemo.main.presentation

import android.graphics.Color
import android.os.Bundle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.szylas.medmemo.R
import com.szylas.medmemo.main.presentation.models.CalendarScreen
import com.szylas.medmemo.main.presentation.models.HomeScreen
import com.szylas.medmemo.main.presentation.models.NavBarItem
import com.szylas.medmemo.common.presentation.style.NavBarItemStyleProvider
import com.szylas.medmemo.main.presentation.models.StatisticsScreen
import com.szylas.medmemo.main.presentation.views.CalendarFragment
import com.szylas.medmemo.main.presentation.views.HomeFragment
import com.szylas.medmemo.main.presentation.views.ProfileFragment
import com.szylas.medmemo.memo.domain.notifications.registerNotificationChannel
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme

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

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor(AppBarBlackCode))
        )

        setContent {
            MedMemoTheme {
                val navController = rememberNavController()

                var selectedBottomIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }


                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onTertiary,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceDim,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    ), title = {
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
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    ) {
                        bottomNavItems.fastForEachIndexed { index, item ->
                            NavigationBarItem(colors = NavBarItemStyleProvider.provide(),
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

}

