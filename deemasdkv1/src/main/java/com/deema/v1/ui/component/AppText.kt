package com.deema.v1.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deema.v1.ui.theme.black_0
import com.deema.v1.ui.theme.black_1

@Composable
fun AppText(
    text: String,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    modifier: Modifier = Modifier,
    color: Color = black_1,
    numOfLine:Int = 3,
    textAlign: TextAlign? = null,
){
    Text(
        text = text,
        style = style,
        modifier = modifier,
        maxLines = numOfLine,
        color = color,
        textAlign = textAlign,
    )
}

