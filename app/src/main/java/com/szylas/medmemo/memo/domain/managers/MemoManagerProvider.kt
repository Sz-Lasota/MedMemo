package com.szylas.medmemo.memo.domain.managers

import com.szylas.medmemo.memo.datastore.FirebaseMemoRepository

object MemoManagerProvider {

    val memoManager = MemoManager(FirebaseMemoRepository())

}