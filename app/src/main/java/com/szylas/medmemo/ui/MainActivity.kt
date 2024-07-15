package com.szylas.medmemo.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.szylas.medmemo.R
import com.szylas.medmemo.ui.common.CalendarScreen
import com.szylas.medmemo.ui.common.HomeScreen
import com.szylas.medmemo.ui.common.NavBarItem
import com.szylas.medmemo.ui.common.NavBarItemStyleProvider
import com.szylas.medmemo.ui.common.StatisticsScreen
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor(AppBarBlackCode))
        )

        setContent {
            MedMemoTheme {
                val navController = rememberNavController()

                var selectedItem by rememberSaveable {
                    mutableIntStateOf(0)
                }
                val items = listOf(
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
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    ) {
                        items.fastForEachIndexed { index, item ->
                            NavigationBarItem(colors = NavBarItemStyleProvider.provide(),
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    navController.navigate(item.destination)
                                },
                                icon = {
                                    Icon(
                                        painterResource(id = if (selectedItem == index) item.selectedIcon else item.unselectedIcon),
                                        contentDescription = item.label
                                    )
                                })
                        }
                    }
                }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        NavHost(navController = navController, startDestination = HomeScreen) {
                            composable<HomeScreen> {
                                Text(text = "Home")
                            }
                            composable<CalendarScreen> {
                                Text(text = "Calendar")
                            }
                            composable<StatisticsScreen> {
                                Text(text = "Stats")
                            }
                        }
                    }
                }
            }
        }
    }

}

