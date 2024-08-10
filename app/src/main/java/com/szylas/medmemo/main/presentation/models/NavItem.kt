package com.szylas.medmemo.main.presentation.models

data class NavBarItem(
    val destination: Any,
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

data class ProfileItem(
    val destination: Class<*>,
    val label: String,
    val icon: Int,
)
