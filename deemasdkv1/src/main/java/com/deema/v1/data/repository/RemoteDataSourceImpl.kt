package com.deema.v1.data.repository


import com.deema.v1.data.util.DataState
import com.deema.v1.data.domian.models.PurchaseOrderRequest
import com.deema.v1.data.domian.models.PurchaseRequestResponseModel
import com.deema.v1.data.retrofit.DeemaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RemoteDataSourceImpl(private val api: DeemaApi) :
    RemoteDataSource {

    override suspend fun getPurchaseOrder(request: PurchaseOrderRequest): Flow<DataState<PurchaseRequestResponseModel>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.getPurchaseOrder(request)
            Timber.d("Response: $response")
            emit(DataState.Success(response))
        } catch (e: Exception){
            e.stackTrace
            Timber.d("Exception: ${e.message}")
            emit(DataState.Error(e, message = e.localizedMessage))
        }
    }
}