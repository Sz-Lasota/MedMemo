package com.szylas.medmemo.main.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import com.szylas.medmemo.auth.presentation.LoginActivity
import com.szylas.medmemo.main.presentation.models.CalendarScreen
import com.szylas.medmemo.main.presentation.models.HomeScreen
import com.szylas.medmemo.main.presentation.models.NavBarItem
import com.szylas.medmemo.common.presentation.style.NavBarItemStyleProvider
import com.szylas.medmemo.main.presentation.models.NavDrawerItem
import com.szylas.medmemo.main.presentation.models.StatisticsScreen
import com.szylas.medmemo.main.presentation.views.CalendarFragment
import com.szylas.medmemo.main.presentation.views.HomeFragment
import com.szylas.medmemo.main.presentation.views.StatisticsFragment
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val drawerNavItems = listOf(
        NavDrawerItem(
            destination = MainActivity::class.java,
            label = "Home",
            icon = R.drawable.home_filled
        ),
        NavDrawerItem(
            destination = LoginActivity::class.java,
            label = "Settings",
            icon = R.drawable.settings
        ), NavDrawerItem(
            destination = LoginActivity::class.java,
            label = "Pill amount",
            icon = R.drawable.pill
        ), NavDrawerItem(
            destination = LoginActivity::class.java,
            label = "Pill pictures",
            icon = R.drawable.camera
        ), NavDrawerItem(
            destination = LoginActivity::class.java, label = "Log out", icon = R.drawable.exit
        )
    )

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

                var selectedDrawerIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = MaterialTheme.colorScheme.surface,
                            drawerContentColor = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp))
                        ) {
                            drawerNavItems.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.onSurface,
                                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                        selectedContainerColor = MaterialTheme.colorScheme.surface,
                                        unselectedIconColor = MaterialTheme.colorScheme.surfaceDim,
                                        unselectedTextColor = MaterialTheme.colorScheme.surfaceDim,
                                    ),
                                    label = { Text(text = item.label) },
                                    selected = index == selectedDrawerIndex,
                                    onClick = {
                                        selectedDrawerIndex = index
//                                    scope.launch { drawerState.close() }
                                        startActivity(Intent(this@MainActivity, item.destination))
                                        if (item.label == "Log out") {
                                            finish()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = item.icon),
                                            contentDescription = item.label,
                                            modifier = Modifier.width(48.dp).height(48.dp)
                                        )
                                    })
                            }
                        }
                    }, drawerState = drawerState
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(
                            colors = TopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onTertiary,
                                scrolledContainerColor = MaterialTheme.colorScheme.surfaceDim,
                                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                                navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            title = {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium,
                                )

                            }, navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
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
                                    StatisticsFragment(activity = this@MainActivity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

