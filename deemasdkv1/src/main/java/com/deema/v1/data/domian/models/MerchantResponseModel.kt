package com.deema.v1.data.domian.models

import com.google.gson.annotations.SerializedName


data class MerchantRequestResponseModel (
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: MerchantRequestResponseData? = null,
)


data class MerchantRequestResponseData (
    @SerializedName("purchase_id")
    val purchaseId: String,
    @SerializedName("redirect_link")
    val redirectLink: String,
    @SerializedName("order_reference")
    val orderReference: String,
)


data class MerchantRequest(
    @SerializedName("merchant_order_id")
    val merchantOrderId: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("currency_code")
    val currencyCode: String,
)
