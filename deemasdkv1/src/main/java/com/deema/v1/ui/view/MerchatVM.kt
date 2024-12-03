package com.deema.v1.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deema.v1.data.util.DataState
import com.deema.v1.data.domian.models.MerchantRequest
import com.deema.v1.data.domian.models.MerchantRequestResponseData
import com.deema.v1.data.repository.RemoteDataSource
import com.deema.v1.util.Event
import com.deema.v1.util.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class ApiResponse {None, Loading, Success, Error, NoInternet, UnAuthorize }

data class UiState(
    val merchantRequestResponseData: MerchantRequestResponseData?= null,
    val paymentSuccess: Int = 0,
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
            delay(4780)
            uiState.update { it.copy(paymentSuccess = value) }
        }
    }

    fun loadMerchantData(request: MerchantRequest) {
        Timber.i("loadMerchantData ${request}")

        viewModelScope.launch {
            repository.merchantDetails(request).collect { state ->
                when (state) {
                    is DataState.Success -> {
                        state.data?.let { response ->
                            uiState.update { it.copy(merchantRequestResponseData = response.data, errorMessage = null, apiResponse = ApiResponse.Success) }
                        }
                    }

                    is DataState.Error -> {
                        //The account of {} phone number had been deleted!
                        val errorMessage = state.toString()
                        viewModelScope.launch {
                            EventBus.sendEvent(Event.ErrorToast(errorMessage))
                        }
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
