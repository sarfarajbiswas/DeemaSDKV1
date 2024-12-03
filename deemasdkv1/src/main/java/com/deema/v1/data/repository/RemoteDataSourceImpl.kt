package com.deema.v1.data.repository


import com.deema.v1.data.util.DataState
import com.deema.v1.data.domian.models.MerchantRequest
import com.deema.v1.data.domian.models.MerchantRequestResponseModel
import com.deema.v1.data.retrofit.DeemaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val api: DeemaApi) :
    RemoteDataSource {

    override suspend fun merchantDetails(request: MerchantRequest): Flow<DataState<MerchantRequestResponseModel>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.merchantDetails(request)
            Timber.d("merchantDetails Response: $response")
            emit(DataState.Success(response))
        } catch (e: Exception){
            e.stackTrace
            Timber.d("merchantDetails Exception: ${e.message}")
            emit(DataState.Error(e, message = e.localizedMessage))
        }
    }
}