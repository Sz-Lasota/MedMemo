package com.szylas.medmemo.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 25.dp,
    enabled: Boolean = true
) {
    ComposableButton(
        text = text,
        textStyle = TextStyle(
            fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
        ),
        buttonColors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = onClick,
        modifier = modifier,
        cornerRadius = cornerRadius,
        enabled = enabled
    )
}

@Suppress("unused")
@Composable
fun ErrorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 25.dp,
    enabled: Boolean = true
) {
    ComposableButton(
        text = text,
        textStyle = TextStyle(
            fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
        ),
        buttonColors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.error,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = onClick,
        modifier = modifier,
        cornerRadius = cornerRadius,
        enabled = enabled
    )
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 25.dp,
    enabled: Boolean = true
) {
    ComposableButton(
        text = text,
        textStyle = TextStyle(
            fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
        ),
        buttonColors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = onClick,
        modifier = modifier,
        cornerRadius = cornerRadius,
        enabled = enabled
    )
}

@Composable
fun BlockButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 25.dp,
    height: Dp = 80.dp
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier
            .height(height),

    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            ),
        )
    }
}

@Composable
private fun ComposableButton(
    text: String,
    textStyle: TextStyle,
    buttonColors: ButtonColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 25.dp,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier
            .height(50.dp),
//        colors = buttonColors,
        enabled = enabled
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = text,
//            style = textStyle
        )
    }
}