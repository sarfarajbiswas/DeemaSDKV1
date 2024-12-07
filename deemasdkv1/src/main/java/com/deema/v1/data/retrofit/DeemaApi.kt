package com.deema.v1.data.retrofit

import com.deema.v1.data.util.EndPoint
import com.deema.v1.data.domian.models.PurchaseOrderRequest
import com.deema.v1.data.domian.models.PurchaseRequestResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface DeemaApi {
    @POST(EndPoint.merchant)
    suspend fun getPurchaseOrder(
        @Body request: PurchaseOrderRequest
    ): PurchaseRequestResponseModel
}