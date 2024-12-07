package com.deema.v1.data.util

import com.deema.v1.Environment
import com.deema.v1.data.AppData

internal object BaseUrl {
    const val BASE_STAGING_URL = "https://staging-api.deema.me"
    const val BASE_PROD_URL = "https://staging-api.deema.me"
}

internal object EndPoint {
    const val merchant = "/api/merchant/v1/purchase"
}

fun baseUrl():String{
    return if (AppData.getInstance().getSharedData().environment == Environment.Sandbox) BaseUrl.BASE_STAGING_URL else BaseUrl.BASE_PROD_URL
}
