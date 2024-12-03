package com.deema.v1.data.retrofit

import com.deema.v1.data.util.EndPoint
import com.deema.v1.data.domian.models.MerchantRequest
import com.deema.v1.data.domian.models.MerchantRequestResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface DeemaApi {
    @POST(EndPoint.merchant)
    suspend fun merchantDetails(
        @Body request: MerchantRequest
    ): MerchantRequestResponseModel

}