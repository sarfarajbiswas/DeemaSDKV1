package com.deema.v1.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun AppImage(modifier: Modifier = Modifier, contentScale:  ContentScale =  ContentScale.Fit, @DrawableRes id: Int){
    Image(
        painter = painterResource(id),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}

