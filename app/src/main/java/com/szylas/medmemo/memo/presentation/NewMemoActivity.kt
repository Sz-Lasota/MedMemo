package com.szylas.medmemo.memo.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.main.presentation.MainActivity
import com.szylas.medmemo.memo.presentation.components.StatusBarManager
import com.szylas.medmemo.memo.presentation.models.MemoDateScreen
import com.szylas.medmemo.memo.presentation.models.MemoNameScreen
import com.szylas.medmemo.memo.presentation.models.MemoSummaryScreen
import com.szylas.medmemo.memo.presentation.models.MemoTimeScreen
import com.szylas.medmemo.memo.presentation.views.MemoDateFragment
import com.szylas.medmemo.memo.presentation.views.MemoNameFragment
import com.szylas.medmemo.memo.presentation.views.MemoSummaryFragment
import com.szylas.medmemo.memo.presentation.views.MemoTimeFragment
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme

class NewMemoActivity : ComponentActivity() {


    private val statusManager = StatusBarManager(
        items = listOf(
            StatusBarManager.StatusBarItem(
                icon = R.drawable.pill, destination = MemoNameScreen
            ),
            StatusBarManager.StatusBarItem(
                icon = R.drawable.camera, destination = MemoTimeScreen
            ),
            StatusBarManager.StatusBarItem(
                icon = R.drawable.settings, destination = MemoDateScreen
            ),
            StatusBarManager.StatusBarItem(
                icon = R.drawable.exit, destination = MemoSummaryScreen
            ),

            )
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor(AppBarBlackCode))
        )

        setContent {
            val navController = rememberNavController()
            MedMemoTheme {
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

                    }, navigationIcon = {
                        IconButton(onClick = {
//                            startActivity(
//                                Intent(this@NewMemoActivity, MainActivity::class.java)
//                            )
                            finish()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Menu"
                            )
                        }
                    })
                }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        val modalState = rememberModalBottomSheetState()
                        var modalShow by remember {
                            mutableStateOf(false)
                        }

                        val memo: Memo by remember {
                            mutableStateOf(getMemo())
                        }
                        Log.d("Memo", memo.name)

                        NavHost(navController = navController, startDestination = MemoNameScreen) {
                            composable<MemoNameScreen> {
                                MemoNameFragment(
                                    activity = this@NewMemoActivity,
                                    statusBarManager = statusManager,
                                    memo = memo
                                ) {
                                    navController.navigate(MemoTimeScreen)
                                    statusManager.updateActive(1)
                                }
                            }
                            composable<MemoTimeScreen> {
                                MemoTimeFragment(
                                    activity = this@NewMemoActivity,
                                    statusBarManager = statusManager,
                                    memo = memo
                                ) {
                                    navController.navigate(MemoDateScreen)
                                    statusManager.updateActive(2)
                                }
                            }
                            composable<MemoDateScreen> {
                                MemoDateFragment(
                                    activity = this@NewMemoActivity,
                                    statusBarManager = statusManager,
                                    memo = memo
                                ) {
                                    navController.navigate(MemoSummaryScreen)
                                    statusManager.updateActive(3)
                                }
                            }
                            composable<MemoSummaryScreen> {
                                MemoSummaryFragment(
                                    activity = this@NewMemoActivity,
                                    statusBarManager = statusManager,
                                    memo = memo
                                ) {
                                    modalShow = true
                                }
                            }

                        }
                        if (modalShow) {
                            ModalBottomSheet(sheetState = modalState, onDismissRequest = {
                                // TODO: Save memo to database
                                modalShow = false
                                startActivity(
                                    Intent(this@NewMemoActivity, MainActivity::class.java)
                                )
                                finish()
                            }) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Spacer(modifier = Modifier.height(35.dp))
                                    Icon(
                                        modifier = Modifier
                                            .width(48.dp)
                                            .height(48.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceTint),
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Check"
                                    )
                                    Text(
                                        text = "Memo created", style = TextStyleProvider.provide(
                                            style = TextStyleOption.TITLE_LARGE
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(35.dp))
                                }
                            }
                        }

                    }

                }
            }
        }
    }

    fun getMemo(): Memo = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> if (intent.getSerializableExtra(
                "MEMO",
                Memo::class.java
            ) != null
        ) intent.getSerializableExtra("MEMO", Memo::class.java)!! else Memo()

        else -> @Suppress("DEPRECATION") (if (intent.getSerializableExtra("MEMO") as? Memo != null) intent.getSerializableExtra(
            "MEMO"
        ) as Memo else Memo())
    }

}