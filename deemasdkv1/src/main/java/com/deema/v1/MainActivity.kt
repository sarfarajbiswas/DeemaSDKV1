package com.deema.v1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.deema.v1.data.AppData
import com.deema.v1.data.domian.models.MerchantRequest
import com.deema.v1.ui.component.SweetToastUtil
import com.deema.v1.ui.theme.DeemaSDKAndroidTheme
import com.deema.v1.ui.view.WebMerchantView
import com.deema.v1.util.Event
import com.deema.v1.util.EventBus
import dagger.hilt.android.AndroidEntryPoint

enum class ToastAlertType {
    Success, Error, Warning, Info
}

data class AlertToastUiState(
    val shouldShowDialog: Boolean = false,
    val toast: Boolean = false,
    val title: String? = null,
    val message: String? = null,
    val toastType: ToastAlertType? = ToastAlertType.Error
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val customerPhone = intent.getStringExtra("customerPhone")
        val currency = intent.getStringExtra("currency")
        val purchaseAmount = intent.getStringExtra("purchaseAmount")
        val sdkKey = intent.getStringExtra("SDKKey")
        val merchantOrderId = intent.getStringExtra("merchantOrderId")

        //"1726", amount: "100.000", currency: "BHD"
        AppData.getInstance().getSharedData().phone = customerPhone ?: ""
        AppData.getInstance().getSharedData().sdkKey = sdkKey ?: ""
        AppData.getInstance().getSharedData().currency = currency ?: "KWD"
        AppData.getInstance().getSharedData().purchaseAmount = purchaseAmount ?: "0.0"
        AppData.getInstance().getSharedData().merchantOrderId = merchantOrderId ?: ""

        enableEdgeToEdge()

        setContent {
            var alertToastUiState by remember {
                mutableStateOf<AlertToastUiState>(AlertToastUiState())
            }

            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(key1 = lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    EventBus.events.collect { event ->
                        when (event) {
                            is Event.ErrorToast -> {
                                alertToastUiState = alertToastUiState.copy(toast = true, message = event.message, toastType = ToastAlertType.Error)
                            }
                            is Event.SuccessToast -> {
                                alertToastUiState = alertToastUiState.copy(toast = true, message = event.message, toastType = ToastAlertType.Success)
                            }
                            is Event.WarningToast -> {
                                alertToastUiState = alertToastUiState.copy(toast = true, message = event.message, toastType = ToastAlertType.Warning)
                            }
                            is Event.InfoToast -> {
                                alertToastUiState = alertToastUiState.copy(toast = true, message = event.message, toastType = ToastAlertType.Info)
                            }

                            ///dialog
                            is Event.ErrorDialog -> {
                                alertToastUiState = alertToastUiState.copy(shouldShowDialog = true, title = event.title, message = event.message, toastType = ToastAlertType.Error)
                            }
                            is Event.SuccessDialog -> {
                                alertToastUiState = alertToastUiState.copy(shouldShowDialog = true, title = event.title, message = event.message, toastType = ToastAlertType.Success)
                            }
                        }
                    }
                }
            }

            DeemaSDKAndroidTheme {
                WebMerchantView(
                    request = MerchantRequest(
                        merchantOrderId = AppData.getInstance().getSharedData().merchantOrderId!!,
                        amount = AppData.getInstance().getSharedData().purchaseAmount!!,
                        currencyCode = AppData.getInstance().getSharedData().currency!!
                    )
                )
            }

            /// toast message
            if (alertToastUiState.toast) {
                when (alertToastUiState.toastType) {
                    ToastAlertType.Error ->
                        SweetToastUtil.SweetError(message = alertToastUiState.message ?: "")

                    ToastAlertType.Info ->
                        SweetToastUtil.SweetInfo(message = alertToastUiState.message ?: "")

                    ToastAlertType.Success ->
                        SweetToastUtil.SweetSuccess(message = alertToastUiState.message ?: "")

                    ToastAlertType.Warning ->
                        SweetToastUtil.SweetWarning(message = alertToastUiState.message ?: "")

                    null -> {}
                }

                alertToastUiState = alertToastUiState.copy(toast = false)
            }
        }
    }
}
