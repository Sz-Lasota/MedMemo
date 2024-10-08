package com.szylas.medmemo.common.presentation

import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.common.domain.models.PrefItem
import com.szylas.medmemo.common.presentation.theme.MedMemoTheme

class SettingsActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var options by remember {
                mutableStateOf(
                    listOf(
                        PrefItem(
                            name = "Simplified UI",
                            value = false
                        )
                    )
                )
            }
            MedMemoTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(10.dp)
                    ) {
                        PrefList(
                            options = options,
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(5.dp, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(10.dp)
                        ) { item ->
                            options = mutableListOf<PrefItem>().apply {
                                addAll(options)
                                remove(item)
                                add(PrefItem(item.name, !item.value))
                            }.toList()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun PrefList(
        options: List<PrefItem>,
        modifier: Modifier = Modifier,
        onItemChange: (PrefItem) -> Unit,
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(options) {
                PrefListItem(it, onItemChange = onItemChange, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    @Composable
    private fun PrefListItem(
        item: PrefItem,
        onItemChange: (PrefItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = item.value, onCheckedChange = {
                onItemChange(item)
            })
        }
    }

}