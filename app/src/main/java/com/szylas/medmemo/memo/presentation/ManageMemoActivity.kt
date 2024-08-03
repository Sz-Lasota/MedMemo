package com.szylas.medmemo.memo.presentation

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.ui.ui.theme.AppBarBlackCode
import com.szylas.medmemo.ui.ui.theme.MedMemoTheme
import java.util.Calendar


val mockedList = listOf(
    Memo(
        name = "First",
        numberOfDoses = 0,
        smartMode = false,
        dosageTime = listOf(4 * 60 + 35, 12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
        finishDate = Calendar.getInstance().also {
            it.add(Calendar.DATE, 14)
        }
    ),
    Memo(
        name = "Second",
        numberOfDoses = 4,
        smartMode = true,
        dosageTime = listOf(12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
    ),
    Memo(
        name = "First",
        numberOfDoses = 4,
        smartMode = false,
        dosageTime = listOf(4 * 60 + 35, 12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
        finishDate = Calendar.getInstance().also {
            it.add(Calendar.DATE, 14)
        }
    ),
    Memo(
        name = "Second",
        numberOfDoses = 4,
        smartMode = true,
        dosageTime = listOf(12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
    ),
    Memo(
        name = "First",
        numberOfDoses = 4,
        smartMode = false,
        dosageTime = listOf(4 * 60 + 35, 12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
        finishDate = Calendar.getInstance().also {
            it.add(Calendar.DATE, 14)
        }
    ),
    Memo(
        name = "Second",
        numberOfDoses = 4,
        smartMode = true,
        dosageTime = listOf(12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
    ),
    Memo(
        name = "First",
        numberOfDoses = 4,
        smartMode = false,
        dosageTime = listOf(4 * 60 + 35, 12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
        finishDate = Calendar.getInstance().also {
            it.add(Calendar.DATE, 14)
        }
    ),
    Memo(
        name = "Second",
        numberOfDoses = 4,
        smartMode = true,
        dosageTime = listOf(12 * 60 + 35, 20 * 60 + 35),
        startDate = Calendar.getInstance(),
    ),
)


class ManageMemoActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor(AppBarBlackCode))
        )

        setContent {
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
                            finish()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, contentDescription = "Menu"
                            )
                        }
                    })
                }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {


                    }
                }
            }
        }
    }
}