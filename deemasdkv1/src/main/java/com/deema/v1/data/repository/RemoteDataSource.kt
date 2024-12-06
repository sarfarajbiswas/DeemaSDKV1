package com.deema.v1.data.repository


import com.deema.v1.data.util.DataState
import com.deema.v1.data.domian.models.MerchantRequest
import com.deema.v1.data.domian.models.MerchantRequestResponseModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun merchantDetails(request: MerchantRequest): Flow<DataState<MerchantRequestResponseModel>>

}