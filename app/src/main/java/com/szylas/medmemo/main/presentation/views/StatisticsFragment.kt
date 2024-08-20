package com.szylas.medmemo.main.presentation.views

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.szylas.medmemo.R
import com.szylas.medmemo.auth.presentation.LoginActivity
import com.szylas.medmemo.common.presentation.style.TextStyleOption
import com.szylas.medmemo.common.presentation.style.TextStyleProvider
import com.szylas.medmemo.main.presentation.MainActivity
import com.szylas.medmemo.main.presentation.models.ProfileItem
import com.szylas.medmemo.statistics.datastore.MissedStatistics
import com.szylas.medmemo.statistics.datastore.TimeStatistics

// TODO: Refactor all of this:
//  1. Encapsulate wantedStats in some wrapper class that will also handle title and plot type
//  2. Statistical interface should not return List<Point> but raw data from db
//  3. Chart provider should be replaced by some interface, probably included in wrapper from pt. 1
//      and that how charts should be generated and plot type chosen.

val wantedStats = listOf(
    TimeStatistics(),
    MissedStatistics(),
    TimeStatistics(),
    MissedStatistics(),
    TimeStatistics(),
    MissedStatistics(),
)

private val profileItems = listOf(
    ProfileItem(
        destination = MainActivity::class.java, label = "Home", icon = R.drawable.home_filled
    ), ProfileItem(
        destination = LoginActivity::class.java, label = "Settings", icon = R.drawable.settings
    ), ProfileItem(
        destination = LoginActivity::class.java, label = "Pill amount", icon = R.drawable.pill
    ), ProfileItem(
        destination = LoginActivity::class.java,
        label = "Pill pictures",
        icon = R.drawable.camera
    ), ProfileItem(
        destination = LoginActivity::class.java, label = "Log out", icon = R.drawable.exit
    )
)


@Composable
fun ProfileFragment(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE)
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(profileItems) {
                ProfileListItem(item = it, activity = activity, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun ProfileListItem(item: ProfileItem, activity: ComponentActivity, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clickable {
                Toast.makeText(activity, "Hello from ${item.label}", Toast.LENGTH_SHORT).show()
//                activity.startActivity(Intent(activity, item.destination))
                activity.finish()
            },
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = item.label,
            style = TextStyleProvider.provide(style = TextStyleOption.TITLE_LARGE)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}