package com.deema.v1.ui.component

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.deema.v1.R
import com.deema.v1.ui.theme.dark_color
import com.deema.v1.ui.theme.white_1
import timber.log.Timber

@Composable
fun AppUserImage(modifier: Modifier = Modifier.size(48.dp), imageUrl: String){
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
//        placeholder = painterResource(R.drawable.profile_placeholder),
//        error = painterResource(R.drawable.profile_placeholder),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .background(color = dark_color),
//        colorFilter = ColorFilter.tint(Color.Blue)
    )
}


@Composable
fun AppUserCoverImage(modifier: Modifier = Modifier
    .fillMaxSize()
    .clip(RoundedCornerShape(0.dp)), imageUrl: String){
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
//        placeholder = painterResource(R.drawable.place_holder),
//        error =  painterResource(R.drawable.place_holder),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,
        modifier = modifier,
        //colorFilter = ColorFilter.tint(Color.Blue)
    )
}


@Composable
fun AppLinkImage(modifier: Modifier = Modifier, painter: Painter , contentScale: ContentScale = ContentScale.Fit, imageUrl: String){
    Timber.i("AppLinkImage: $imageUrl")
    Image(painter = painter, contentDescription = "")

//    AsyncImage(
//        model = imageUrl,
//        contentDescription = stringResource(R.string.app_name),
//        contentScale = contentScale,
//        modifier = modifier,
//    )
}

@Composable
fun AppLinkImage(modifier: Modifier = Modifier, contentScale:  ContentScale = ContentScale.Fit, imageUrl: String){
    Timber.i("AppLinkImage: $imageUrl")
    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(imageUrl) //Uri.parse(imageUrl)
//            .crossfade(true)
//            .build(),
        model = imageUrl,
//        model = Uri.parse("asset:///Images/LA_61b.jpg"),
//        placeholder = painterResource(R.drawable.place_holder),
//        error =  painterResource(R.drawable.place_holder),
        contentDescription = stringResource(R.string.app_name),
        contentScale = contentScale,
        modifier = modifier,
    )
}


@Composable
fun AppLinkImage(modifier: Modifier = Modifier, imageUrl: String){
    Timber.i("AppLinkImage: $imageUrl")

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
//        placeholder = painterResource(R.drawable.place_holder),
//        error =  painterResource(R.drawable.place_holder),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Fit,
        modifier = modifier,
    )
}


@Composable
fun AppImage(modifier: Modifier = Modifier, contentScale:  ContentScale =  ContentScale.Fit, @DrawableRes id: Int){
    Image(
        painter = painterResource(id),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}

@Composable
fun AppImageVector(modifier: Modifier = Modifier, color: Color = white_1, icon: ImageVector){
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = modifier,
    )
}


//:


