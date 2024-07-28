package com.szylas.medmemo.memos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class StatusBarManager(
    private val items: List<StatusBarItem>,
    private var active: Int = 0
) {

    fun updateActive(value: Int) {
        if (active > value) {
            return
        }
        active = value
    }

    data class StatusBarItem(
        val icon: Int,
        val destination: Any
    )

    @Composable
    fun StatusBar(
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(1f))

            items.forEachIndexed { index, it ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (index >= active) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondary)
                        .border(2.dp, MaterialTheme.colorScheme.secondary),
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp),
                        painter = painterResource(it.icon),
                        contentDescription = it.destination.toString()
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}