package com.szylas.medmemo.ui.common

data class NavBarItem(
    val destination: Any,
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)
