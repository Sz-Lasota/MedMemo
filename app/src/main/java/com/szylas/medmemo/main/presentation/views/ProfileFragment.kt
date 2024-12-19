package com.szylas.medmemo.main.presentation.views

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.auth.domain.Session
import com.szylas.medmemo.main.presentation.SettingsActivity
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.main.presentation.models.HomeScreen
import com.szylas.medmemo.main.presentation.models.ProfileItem
import com.szylas.medmemo.memo.presentation.PillAmountActivity
import com.szylas.medmemo.statistics.presentation.StatisticsActivity


private val profileItems = listOf(
    ProfileItem(
        navigate = { it.startActivity(Intent(it, SettingsActivity::class.java)) },
        label = "Settings",
        icon = R.drawable.settings
    ),
    ProfileItem(
        navigate = { it.startActivity(Intent(it, StatisticsActivity::class.java)) },
        label = "Statistics",
        icon = R.drawable.baseline_bar_chart_24
    ),
    ProfileItem(
        navigate = {
            it.startActivity(Intent(it, PillAmountActivity::class.java))
        },
        label = "Pill amount",
        icon = R.drawable.pill
    ), ProfileItem(
        navigate = { },
        label = "Pill pictures",
        icon = R.drawable.camera
    ), ProfileItem(
        navigate = {
            Session.signOut()
            it.finish()
        },
        label = "Log out",
        icon = R.drawable.exit
    )
)

@Composable
fun SimpleProfileFragment(activity: ComponentActivity, navigate: (Any) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp)
        ) {
            items(profileItems) {
                ProfileListItem(item = it, activity, modifier = Modifier.fillMaxWidth())
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable { navigate(HomeScreen) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp),
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home"
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    Text(
                        text = "Home",
                        style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}



@Composable
fun ProfileFragment(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp)
        ) {
            items(profileItems) {
                ProfileListItem(item = it, activity, modifier = Modifier.fillMaxWidth())
            }

        }
    }
}

@Composable
fun ProfileListItem(item: ProfileItem, activity: ComponentActivity, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { item.navigate(activity) },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            painter = painterResource(item.icon),
            contentDescription = item.label
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = item.label,
            style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next"
        )
    }
}