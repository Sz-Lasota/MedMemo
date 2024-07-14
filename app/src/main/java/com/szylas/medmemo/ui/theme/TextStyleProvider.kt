package com.szylas.medmemo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle


enum class TextStyleOption {
    LABEL_MEDIUM, TITLE_LARGE
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
        }
    }

}