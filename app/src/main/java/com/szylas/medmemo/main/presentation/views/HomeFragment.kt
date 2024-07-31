package com.szylas.medmemo.main.presentation.views

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.common.presentation.components.BlockButton
import com.szylas.medmemo.memos.presentation.NewMemoActivity


@Composable
fun HomeFragment(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            Text(text="Hello in", style= TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM))
            Text(text="MedMemo", style= TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE))
            Text(text="\$placeholder", style= TextStyleProvider.provide(style = TextStyleOption.LABEL_MEDIUM))
        }
        Spacer(modifier = Modifier.weight(1f))

        BlockButton(
            text = "New memo",
            onClick = { activity.startActivity(
                Intent(
                    activity, NewMemoActivity::class.java
                ).also {
                    it.putExtra("MEMO", Memo(name = "Hello from memo"))
                }
            ) },
            modifier = Modifier.fillMaxWidth()
        )
        BlockButton(
            text = "Manage memos",
            onClick = { Toast.makeText(activity, "New memo", Toast.LENGTH_LONG).show() },
            modifier = Modifier.fillMaxWidth()
        )
        BlockButton(
            text = "Read my CMI",
            onClick = { Toast.makeText(activity, "New memo", Toast.LENGTH_LONG).show() },
            modifier = Modifier.fillMaxWidth()
        )
        BlockButton(
            text = "Help",
            onClick = { Toast.makeText(activity, "New memo", Toast.LENGTH_LONG).show() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}