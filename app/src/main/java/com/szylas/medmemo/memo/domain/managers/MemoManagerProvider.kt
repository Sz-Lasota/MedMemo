package com.szylas.medmemo.memo.domain.managers

import com.szylas.medmemo.memo.datastore.FirebaseMemoSaver

object MemoManagerProvider {

    val memoManager = MemoManager(FirebaseMemoSaver())

}