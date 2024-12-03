package com.deema.v1.ui.view

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deema.v1.R
import com.deema.v1.data.AppConstants
import com.deema.v1.data.domian.models.MerchantRequest
import com.deema.v1.ui.component.AppDialogPopup
import com.deema.v1.ui.component.AppText
import com.deema.v1.ui.component.PayFailedView
import com.deema.v1.ui.component.PaySuccessView
import com.deema.v1.ui.theme.errorColor
import com.deema.v1.ui.theme.primaryColor
import timber.log.Timber

@Composable
fun WebMerchantView(request: MerchantRequest){
    val viewModel = hiltViewModel<MerchantVM>()
    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit){
        viewModel.loadMerchantData(
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
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(28.dp), contentAlignment = Alignment.Center) {
                            AppText(text = context.getString(R.string.generalError), color = errorColor)
                        }
                    }
                }
            }
        }
    )

    ///
    val activity = (LocalContext.current as? Activity)

    if(uiState.paymentSuccess == 1){
        activity?.finish()
//        AppDialogPopup(content = {
//            PaySuccessView()
//        }) {
//            viewModel.changePaymentStatus(0)
//        }
    }
    if(uiState.paymentSuccess == 2){
        activity?.finish()
//        AppDialogPopup(content = {
//            PayFailedView()
//        }) {
//            viewModel.changePaymentStatus(0)
//        }
    }
}


@Composable
private fun ContentUI(viewModel: MerchantVM, uiState: UiState) {
    val context = LocalContext.current
    val urlString = uiState.merchantRequestResponseData?.redirectLink ?: ""
        //"https://stackoverflow.com/questions/78561521/how-to-properly-navigate-a-webview-when-the-base-url-isnt-changing-jetpack-comp"

    WebViewPage(viewModel, urlString)
}


@Composable
@SuppressLint("SetJavaScriptEnabled")
fun WebViewPage(viewModel: MerchantVM, urlString: String){
    Timber.d("WebViewPage: $urlString")
    //https:\/\/staging-pay.deema.me\/?order_reference=8605b3cd-7679-4285-8f65-e1c52124fb15
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
                        if (url.contains(AppConstants.successUrl) || url == "https://staging.deema.me/") {
                            viewModel.changePaymentStatus(1)
                        } else if (url.contains(AppConstants.failureUrl) || url == "https://staging.deema.me/") {
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





/*
class MainActivity : AppCompatActivity() {

    private val url = "https://stackoverflow.com/"
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webview)
        // Get the web view settings instance
        val settings = webView.settings

        // Enable java script in web view
        settings.javaScriptEnabled = true

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


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

        // WebView settings
        webView.fitsSystemWindows = true


        /*
            if SDK version is greater of 19 then activate hardware acceleration
            otherwise activate software acceleration
        */
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)


        // Set web view client
        webView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
            }
        }
        webView.loadUrl(url);


        fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.contains("stackoverflow.com")) {
                view.loadUrl(url)
            } else {


 */