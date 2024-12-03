package com.deema.v1.data.di

import android.content.Context
import com.deema.v1.data.AppData
import com.deema.v1.data.SessionManager
import com.deema.v1.extensions.fromPrettyJson
import com.deema.v1.data.AppConstants
import com.deema.v1.data.domian.models.ApiResponseModel
import com.deema.v1.util.Event
import com.deema.v1.util.EventBus
import com.deema.v1.util.EventBusUnAuthorize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import timber.log.Timber

class AuthInterceptor(private val context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {

        ///
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("source", "sdk")

        requestBuilder.addHeader("Authorization", "Basic ${AppData.getInstance().getSharedData().sdkKey}")


        Timber.i("API request header: ${requestBuilder.toString()}")
        //return chain.proceed(requestBuilder.build())

        ///
        try {
            val response = chain.proceed(requestBuilder.build())
            // line below caused the crash
            Timber.i("API Response Code -> ${response.code}")

            if(response.code == 401){
                handleUnauthorizedEvent()
            }

            handleError(response)

            return response
        }catch (e: IOException){
            Timber.d("Exception: $e")
            handleIOError(e)

            throw e
        }
    }

    private fun handleUnauthorizedEvent() {
        CoroutineScope(Dispatchers.Default).launch{
            EventBus.sendEvent(Event.ErrorToast("Unauthorized access"))
        }
    }

    private fun handleError(response: Response) {
        try {
            if(response.code == 200) return
            if(response.code == 502) return
            if(response.code == 401) return

            var errMessage = AppConstants.generalError
            val responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            val jsonStr = responseBodyCopy.string()

            if(!jsonStr.isNullOrEmpty()){
                val apiResponse = jsonStr?.fromPrettyJson<ApiResponseModel>()
                if(apiResponse != null){
                    errMessage = apiResponse?.message ?: (apiResponse?.error ?: AppConstants.generalError)
                }
            }

            Timber.d("API Error: $errMessage")

            CoroutineScope(Dispatchers.IO).launch{
                EventBus.sendEvent(Event.ErrorToast(message = errMessage))
            }
        }catch (e: Exception){
            Timber.d("Exception: $e")
            throw e
        }

    }

    private fun handleIOError(e: IOException) {
        var errMessage = AppConstants.generalError

        CoroutineScope(Dispatchers.Default).launch{
            EventBus.sendEvent(Event.ErrorToast(message = errMessage))
        }
    }
}