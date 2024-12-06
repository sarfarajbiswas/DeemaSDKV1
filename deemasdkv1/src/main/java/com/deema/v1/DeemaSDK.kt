package com.deema.v1

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.deema.v1.data.AppData
import com.google.gson.annotations.SerializedName

enum class Environment{
    Sandbox, Production
}

//enum class PaymentStatus{
//    Success, Failure, Canceled, Unauthorized ,Unknown,
//}

sealed interface PaymentStatus {
    data object Success : PaymentStatus
    data object Canceled : PaymentStatus
    data class Failure(val message: String?) : PaymentStatus
    data class Unknown(val message: String?) : PaymentStatus
}
class DeemaSDK{
    companion object{
        fun launch(environment: Environment, context: Context, currency: String, purchaseAmount: String, sdkKey: String, merchantOrderId: String, launcher: ActivityResultLauncher<String>){
            val appData = AppData.getInstance().getSharedData()
            appData.environment = environment
            appData.sdkKey = sdkKey ?: ""
            appData.currency = currency ?: "KWD"
            appData.purchaseAmount = purchaseAmount ?: "0.0"
            appData.merchantOrderId = merchantOrderId ?: ""

            launcher.launch("Deema")
        }
    }
}


class DeemaSDKResult: ActivityResultContract<String, PaymentStatus>(){
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, MainActivity::class.java).putExtra("input", input)
    }
    override fun parseResult(resultCode: Int, intent: Intent?): PaymentStatus {
        if (resultCode == Activity.RESULT_OK) {

            return when(intent?.getStringExtra("status")?.lowercase()){
                "success" -> PaymentStatus.Success
                "failure" -> PaymentStatus.Failure(intent.getStringExtra("message"))
                "canceled" -> PaymentStatus.Canceled
                else -> PaymentStatus.Unknown(intent?.getStringExtra("message"))
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return PaymentStatus.Canceled
        }
        return PaymentStatus.Unknown("Unknown error")
    }
}

