package com.deema.v1.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object EventBus {
    private val _events = Channel<Any>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: Any) {
        _events.send(event)
    }
}

object EventBusUnAuthorize {
    private val _events = Channel<Any>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: Any) {
        _events.send(event)
    }
}

sealed interface Event {
    data class Toast(val message: String) : Event
    data class SuccessToast(val message: String) : Event
    data class ErrorToast(val message: String) : Event
    data class WarningToast(val message: String) : Event
    data class InfoToast(val message: String) : Event
    data class UnAuthorize(val message: String? = null) : Event

    data class ShowLoading(val message: String? = null) : Event
    data class HideLoading(val message: String?= null) : Event

    data class SuccessDialog(val title: String? = null, val message: String) : Event
    data class ErrorDialog(val title: String? = null, val message: String) : Event
    data class WarningDialog(val title: String? = null, val message: String) : Event
    data class InfoDialog(val title: String? = null, val message: String) : Event

}


