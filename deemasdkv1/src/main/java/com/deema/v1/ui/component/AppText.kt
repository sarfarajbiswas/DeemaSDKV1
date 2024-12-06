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

@Composable
fun AppMultilineText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodySmall.copy(color = black_0),
    @DrawableRes leadingIconId: Int ? = null,
    @DrawableRes trailingIconId: Int ? = null,
    modifier: Modifier = Modifier,
){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier, ) {

        leadingIconId?.let {
            Image(
                painter = painterResource(id = leadingIconId),
                modifier = Modifier.size(24.dp).padding(end = 5.dp),
                contentDescription = null,)
        }

        Text(
            text = text,
            modifier = Modifier.padding(start = 0.dp),
            style = style,
        )
        trailingIconId?.let {
            Image(
                painter = painterResource(id = trailingIconId),
                modifier = Modifier.size(24.dp).padding(start = 5.dp),
                contentDescription = null,)
        }
    }
}
