package com.deema.v1.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deema.v1.data.util.DataState
import com.deema.v1.data.domian.models.PurchaseDetails
import com.deema.v1.data.domian.models.PurchaseOrderRequest
import com.deema.v1.data.repository.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class ApiResponse {None, Loading, Success, Error, NoInternet, UnAuthorize }

data class UiState(
    val merchantRequestResponseData: PurchaseDetails?= null,
    val status: Int = 0,
    val errorMessage: String? = null,
    val apiResponse: ApiResponse = ApiResponse.None,
    )


@HiltViewModel
class MerchantVM @Inject constructor(private val repository: RemoteDataSource) : ViewModel(){

    var uiState = MutableStateFlow(UiState())
        private set

    fun changePaymentStatus(value: Int) {
        Timber.i("changePaymentStatus $value")
        viewModelScope.launch {
            delay(500)
            uiState.update { it.copy(status = value) }
        }
    }

    fun merchantDetails(request: PurchaseOrderRequest) {
        Timber.i("loadMerchantData $request")

        viewModelScope.launch {
            repository.getPurchaseOrder(request).collect { state ->
                when (state) {
                    is DataState.Success -> {
                        state.data?.let { response ->
                            uiState.update { it.copy(merchantRequestResponseData = response.data, errorMessage = null, apiResponse = ApiResponse.Success) }
                        }
                    }

                    is DataState.Error -> {
                        val errorMessage = state.toString()
                        uiState.update { it.copy(errorMessage = errorMessage, apiResponse = ApiResponse.Error) }
                    }

                    is DataState.Loading -> {
                        uiState.update { it.copy(apiResponse = ApiResponse.Loading) }
                    }
                }
            }
        }
    }
}
