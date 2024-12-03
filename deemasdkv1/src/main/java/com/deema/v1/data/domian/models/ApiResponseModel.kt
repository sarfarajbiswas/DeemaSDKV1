package com.deema.v1.data.domian.models

import com.google.gson.annotations.SerializedName


data class ApiResponseModel (
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: Any?,
    @SerializedName("errors")
    val errors: Any?,
    @SerializedName("error")
    val error: String?,
)

