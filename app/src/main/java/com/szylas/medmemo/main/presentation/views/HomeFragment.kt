package com.szylas.medmemo.main.presentation.views

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szylas.medmemo.R
import com.szylas.medmemo.common.domain.formatters.formatDate
import com.szylas.medmemo.common.domain.formatters.formatTime
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification
import com.szylas.medmemo.main.domain.getUpcomingNotifications
import com.szylas.medmemo.main.presentation.models.StatisticsScreen
import com.szylas.medmemo.memo.presentation.ManageMemoActivity
import com.szylas.medmemo.memo.presentation.MemoTakenActivity
import com.szylas.medmemo.memo.presentation.NewMemoActivity


@Composable
fun SimpleHomeFragment(activity: ComponentActivity, navigate: (Any) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                activity.startActivity(
                    Intent(
                        activity, NewMemoActivity::class.java
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                stringResource(id = R.string.new_memo),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { activity.startActivity(Intent(activity, UpcomingNotificationsActivity::class.java)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                "Today notifications",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { activity.startActivity(Intent(activity, ManageMemoActivity::class.java)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                "Manage Memos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { navigate(StatisticsScreen) },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                "Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun HomeFragment(memos: List<Memo>?, activity: ComponentActivity) {
    if (memos == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        item {
            ManageMemoBlock(
                memos = memos,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(10.dp),
                activity = activity
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(10.dp)
                    .clickable {
                        activity.startActivity(
                            Intent(
                                activity, NewMemoActivity::class.java
                            )
                        )
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.new_memo)
                )
                Text(
                    text = stringResource(id = R.string.new_memo), textAlign = TextAlign.Center
                )
            }
        }

        item {
            NextNotificationsBlock(
                memos = memos,
                activity = activity,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(10.dp)
            )
        }


    }

}

@Composable
fun NextNotificationsBlock(
    activity: ComponentActivity,
    memos: List<Memo>,
    modifier: Modifier = Modifier,
) {

    val upcomingNotifications = getUpcomingNotifications(memos)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = stringResource(R.string.upcoming_notifications),
            style = MaterialTheme.typography.headlineLarge
        )

        if (upcomingNotifications.isEmpty()) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(
                text = stringResource(R.string.there_are_no_upcoming_notifications),
                style = MaterialTheme.typography.labelLarge
            )
            return
        }

        upcomingNotifications.forEach { (memo, notification) ->
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            NotificationItem(
                memo = memo,
                notification = notification,
                activity = activity,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}


@Composable
fun ManageMemoBlock(activity: ComponentActivity, memos: List<Memo>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Text(
            text = stringResource(R.string.current_memos),
            style = MaterialTheme.typography.headlineLarge
        )
        if (memos.isEmpty()) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(
                text = stringResource(R.string.there_are_no_current_memos),
                style = MaterialTheme.typography.labelLarge
            )
            return
        }
        memos.take(memos.size.coerceAtMost(6)).forEach { memo ->
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            MemoItem(memo = memo, modifier = Modifier.fillMaxWidth())
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    activity.startActivity(Intent(activity, ManageMemoActivity::class.java))
                }, horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.see_more),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun MemoItem(memo: Memo, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Text(text = memo.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = if (memo.finishDate == null) stringResource(R.string.endless) else stringResource(
                R.string.ends_on, formatDate(memo.finishDate!!)
            ), style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun NotificationItem(
    memo: Memo,
    notification: MemoNotification,
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.at, notification.name, formatTime(notification.date)),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            activity.startActivity(Intent(activity, MemoTakenActivity::class.java).apply {
                putExtra("MEMO", memo)
                putExtra("NOTIFICATION", notification)
            })
        }) {
            Text(
                text = stringResource(R.string.take),
                maxLines = 1,
            )
        }
    }
}