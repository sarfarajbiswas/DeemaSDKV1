package com.deema.v1.data

import android.content.Context

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
    var token: String? = null,
    var sdkKey: String? = AppConstants.sdkKey,
    var purchaseAmount: String? = "20",
    var currency: String? = "KWD",
    var phone: String? = null,
    var merchantContext: Context? = null,
    var merchantOrderId: String? = null,
    var deemaOrderReference: String? = null,
    var isAvailable: Boolean = false,
    var termsAccepted: Boolean = false,
) {
    fun clearData() {
        token = null;
    }
}
