package com.szylas.medmemo.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable


object NavBarItemStyleProvider {

    @Composable
    fun provide(): NavigationBarItemColors {
        return NavigationBarItemColors(
            selectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSecondary,
            selectedIndicatorColor = MaterialTheme.colorScheme.surface,
            unselectedIconColor = MaterialTheme.colorScheme.outline,
            unselectedTextColor = MaterialTheme.colorScheme.outline,
            disabledIconColor = MaterialTheme.colorScheme.primary,
            disabledTextColor = MaterialTheme.colorScheme.primary,
        )
    }

}