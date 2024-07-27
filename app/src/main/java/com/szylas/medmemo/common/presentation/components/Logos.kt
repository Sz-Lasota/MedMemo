package com.szylas.medmemo.common.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.szylas.medmemo.R

@Composable
fun AppLogo(modifier: Modifier = Modifier, color: Color = Color.White) {
    Icon(
        painter = painterResource(id = R.drawable.ic_app_img),
        contentDescription = "ImageLogo",
        modifier = modifier,
        tint = color
    )
}