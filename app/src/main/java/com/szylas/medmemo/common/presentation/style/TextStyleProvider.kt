package com.szylas.medmemo.common.presentation.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle


enum class TextStyleOption {
    LABEL_MEDIUM, TITLE_LARGE, LABEL_LARGE, LABEL_SMALL
}

object TextStyleProvider {

    @Composable
    fun provide(style: TextStyleOption): TextStyle {
        return when (style) {
            TextStyleOption.LABEL_MEDIUM -> TextStyle(
                fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )
            TextStyleOption.TITLE_LARGE -> TextStyle(
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
            TextStyleOption.LABEL_LARGE -> TextStyle(
                fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            )
            TextStyleOption.LABEL_SMALL -> TextStyle(
                fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }
    }

}