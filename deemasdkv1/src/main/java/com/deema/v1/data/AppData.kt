package com.deema.v1.data

import android.content.Context
import com.deema.v1.Environment

class AppData private constructor() {
    companion object {
        @Volatile private var instance: AppData? = null //Volatile modifier is necessary
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AppData().also { instance = it }
            }
    }

    private var sharedData: SharedData?  = null

    fun getSharedData(): SharedData {
        if (sharedData == null){
            sharedData = SharedData()
        }

        return sharedData!!
    }
}


data class SharedData(
    var environment: Environment? = Environment.Sandbox,
    var sdkKey: String? = null,
    var purchaseAmount: String? = "",
    var currency: String? = "KWD",
    var merchantContext: Context? = null,
    var merchantOrderId: String? = null,
    var deemaOrderReference: String? = null,
)
