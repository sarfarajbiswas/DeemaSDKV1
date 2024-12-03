package com.deema.v1.data.util

/**
 * Data state for processing api response Loading, Success and Error
 */
sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception, val message: String? = null) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
}