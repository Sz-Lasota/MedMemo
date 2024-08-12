package com.szylas.medmemo.main.presentation.views

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.presentation.components.BlockButton
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.memo.presentation.ManageMemoActivity
import com.szylas.medmemo.memo.presentation.NewMemoActivity


@Composable
fun HomeFragment(activity: ComponentActivity) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
        ) {
            BlockButton(
                text = stringResource(R.string.new_memo),
                onClick = { activity.startActivity(
                    Intent(
                        activity, NewMemoActivity::class.java
                    )
                ) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            BlockButton(
                text = stringResource(R.string.manage_memos),
                onClick = { activity.startActivity(Intent(activity, ManageMemoActivity::class.java)) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
        ) {
            BlockButton(
                text = "Read my CMI",
                onClick = { Toast.makeText(activity, "New memo", Toast.LENGTH_LONG).show() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            BlockButton(
                text = "Help",
                onClick = { Toast.makeText(activity, "New memo", Toast.LENGTH_LONG).show() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
        Spacer(modifier = Modifier.weight(0.2f))

    }
}