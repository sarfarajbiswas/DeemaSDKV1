package com.deema.v1.data.repository


import com.deema.v1.data.domian.models.PurchaseOrderRequest
import com.deema.v1.data.util.DataState
import com.deema.v1.data.domian.models.PurchaseRequestResponseModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getPurchaseOrder(request: PurchaseOrderRequest): Flow<DataState<PurchaseRequestResponseModel>>
}