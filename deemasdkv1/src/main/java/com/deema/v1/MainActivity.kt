package com.deema.v1

import android.content.Intent
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
import com.deema.v1.data.di.AppNetworkModule
import com.deema.v1.data.di.AppNetworkModuleImpl
import com.deema.v1.data.domian.models.PurchaseOrderRequest
import com.deema.v1.ui.theme.DeemaSDKAndroidTheme
import com.deema.v1.ui.view.WebMerchantView
import com.deema.v1.util.Event
import com.deema.v1.util.EventBus

data class ErrorUiState(
    val isError: Boolean = false,
    val title: String? = null,
    val message: String? = null,
)

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var appModule: AppNetworkModule
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appModule = AppNetworkModuleImpl(this)

        enableEdgeToEdge()

        setContent {
            var errorUiState by remember {
                mutableStateOf<ErrorUiState>(ErrorUiState())
            }

            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(key1 = lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    EventBus.events.collect { event ->
                        when (event) {
                            is Event.Error -> {
                                errorUiState = errorUiState.copy(isError = true, message = event.message)
                            }
                        }
                    }
                }
            }

            DeemaSDKAndroidTheme {
                WebMerchantView(
                    request = PurchaseOrderRequest(
                        merchantOrderId = AppData.getInstance().getSharedData().merchantOrderId!!,
                        amount = AppData.getInstance().getSharedData().purchaseAmount!!,
                        currencyCode = AppData.getInstance().getSharedData().currency!!
                    )
                )
            }

            /// handle error events
            if (errorUiState.isError) {
                MainActivity@this.setResult(RESULT_OK, Intent().apply {
                    putExtra("status", "failure")
                    putExtra("message", errorUiState.message)
                })
                MainActivity@this.finish()
            }
        }
    }
}
