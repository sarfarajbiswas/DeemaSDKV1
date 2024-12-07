package com.deema.v1.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.deema.v1.ui.theme.offWhiteColor
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deema.v1.data.AppConstants
import com.deema.v1.data.domian.models.PurchaseOrderRequest
import com.deema.v1.ui.component.AppText
import com.deema.v1.ui.theme.primaryColor
import timber.log.Timber

@Composable
fun WebMerchantView(request: PurchaseOrderRequest){
    val viewModel = hiltViewModel<MerchantVM>()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit){
        viewModel.merchantDetails(
            request = request
        )
    }

    Scaffold(
        containerColor = offWhiteColor,
        content = { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 0.dp)
            ) {
                when(uiState.apiResponse){
                    ApiResponse.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            AppBigCircularProgress(color = primaryColor)
                        }
                    }
                    ApiResponse.Success -> {
                        ContentUI(viewModel, uiState)
                    }
                    else -> {
                        AppText(text = "")
                    }
                }
            }
        }
    )

    ///
    val activity = (LocalContext.current as? Activity)

    when(uiState.status){
        1 -> {
            activity?.setResult(RESULT_OK, Intent().apply {
                putExtra("status", "success")
                putExtra("message", "Payment Successful")
            })
            activity?.finish()
        }
        2 -> {
            activity?.setResult(RESULT_OK, Intent().apply {
                putExtra("status", "failure")
                putExtra("message", uiState.errorMessage)
            })
            activity?.finish()
        }
        3 ->{
            activity?.setResult(RESULT_OK, Intent().apply {
                putExtra("status", "failure")
                putExtra("message", uiState.errorMessage)
            })
            activity?.finish()
        }
        else -> null
    }
}


@Composable
private fun ContentUI(viewModel: MerchantVM, uiState: UiState) {
    val urlString = uiState.merchantRequestResponseData?.redirectLink ?: ""
    WebViewPage(viewModel, urlString)
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
fun WebViewPage(viewModel: MerchantVM, urlString: String){
    Timber.d("WebViewPage: $urlString")
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient =  object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Timber.d("shouldOverrideUrlLoading: $url")

                    if (url != null) {
                        if (url == AppConstants.successUrl) {
                            viewModel.changePaymentStatus(1)
                        } else if (url == AppConstants.failureUrl) {
                            viewModel.changePaymentStatus(2)
                        }

                        view?.loadUrl(url)
                    }

                    return true
                }
            }

            ////
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)

            // to verify that the client requesting your web page is actually your Android app.
            settings.userAgentString = System.getProperty("http.agent") //Dalvik/2.1.0 (Linux; U; Android 11; M2012K11I Build/RKQ1.201112.002)

            // Enable zooming in web view
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            settings.displayZoomControls = false

            // Zoom web view text
            settings.textZoom = 100

            // Enable disable images in web view
            settings.blockNetworkImage = false
            // Whether the WebView should load image resources
            settings.loadsImagesAutomatically = true

            // More web view settings
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                settings.safeBrowsingEnabled = true  // api 26
            }
            //settings.pluginState = WebSettings.PluginState.ON
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false


            // More optional settings, you can enable it by yourself
            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            settings.loadWithOverviewMode = true
            settings.allowContentAccess = true
            settings.setGeolocationEnabled(true)
            settings.allowUniversalAccessFromFileURLs = true
            settings.allowFileAccess = true

            loadUrl(urlString)
        }
    }, update = {
        it.loadUrl(urlString)
    })
}

