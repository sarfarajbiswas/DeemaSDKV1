package com.deema.v1.ui.view

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deema.v1.ui.theme.primaryColor

@Composable
fun AppBigCircularProgress(modifier: Modifier = Modifier.padding(1.dp).size(36.dp), color: Color = primaryColor) {
    CircularProgressIndicator(
        color = color,
        strokeWidth = 3.5.dp,
        modifier = modifier,
    )
}