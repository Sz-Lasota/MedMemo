package com.szylas.medmemo.main.presentation.models

import androidx.activity.ComponentActivity

data class NavBarItem(
    val destination: Any,
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

data class ProfileItem(
    val navigate: (ComponentActivity) -> Unit,
    val label: String,
    val icon: Int,
)
