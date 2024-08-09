package com.szylas.medmemo.memo.domain.extensions

import android.content.Intent
import android.os.Build
import com.szylas.medmemo.common.domain.models.Memo
import com.szylas.medmemo.common.domain.models.MemoNotification

fun Intent.getNotification(): MemoNotification = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
        if (getSerializableExtra(
                "NOTIFICATION", MemoNotification::class.java
            ) != null
        ) getSerializableExtra(
            "NOTIFICATION", MemoNotification::class.java
        )!! else MemoNotification()
    }

    else -> {
        @Suppress("DEPRECATION") if (getSerializableExtra("NOTIFICATION") as? MemoNotification != null) getSerializableExtra(
            "NOTIFICATION"
        ) as MemoNotification
        else MemoNotification()
    }
}

fun Intent.getMemo(): Memo = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
        if (getSerializableExtra(
                "MEMO", Memo::class.java
            ) != null
        ) getSerializableExtra("MEMO", Memo::class.java)!! else Memo()
    }

    else -> {
        @Suppress("DEPRECATION") if (getSerializableExtra("MEMO") as? Memo != null) getSerializableExtra(
            "MEMO"
        ) as Memo
        else Memo()
    }
}