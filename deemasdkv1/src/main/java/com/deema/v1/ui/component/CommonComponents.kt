package com.deema.v1.ui.component


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deema.v1.R
import com.deema.v1.ui.theme.black_1

@Composable
fun PayFailedView(modifier: Modifier = Modifier.fillMaxSize()){
    Box(modifier = modifier.padding(horizontal = 24.dp), contentAlignment = Alignment.Center){
        StatusComponent(title = "Payment Failed", subtitle = "Unfortunately, your installment payment could not be processed. Please verify your payment details and try again.", topIcon = {
            AppImage(id = R.drawable.wrong, modifier = Modifier.size(36.dp * 1.2f))
        })
    }
}


@Composable
fun PaySuccessView(modifier: Modifier = Modifier.fillMaxSize()){

    Box(modifier = modifier.padding(horizontal = 24.dp), contentAlignment = Alignment.Center){
        StatusComponent(title = "Payment Successful", subtitle = "Thank you! Your installment payment of 120 KWD has been successfully processed.", topIcon = {
            AppImage(id = R.drawable.correct, modifier = Modifier.size(36.dp * 1.2f))
        })
    }
}



@Composable
fun StatusComponent(modifier: Modifier = Modifier.fillMaxWidth(), topIcon: @Composable (() -> Unit) = {}, title:String, subtitle: String, styleTitle: TextStyle =  MaterialTheme.typography.labelLarge.copy(color = black_1), styleSubTitle: TextStyle =  MaterialTheme.typography.labelMedium.copy(color = black_1),){
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        topIcon()

        Spacer(modifier = Modifier.height(10.dp))

        AppMultilineText(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            text = title,
            style = styleTitle,
        )
        AppMultilineText(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 2.dp),
            text = subtitle,
            style = styleSubTitle.copy(textAlign = TextAlign.Center),
        )
    }
}
