package com.deema.v1.data.util

internal object BaseUrl {
    const val BASE_STAGING_URL = "https://staging-api.deema.me"
//    const val BASE_PROD_URL = "https://staging-api.deema.me"

}

internal object ApiVersion {
    const val VERSION = "sdk/v1/"
}

internal object EndPoint {
    const val merchant = "/api/merchant/v1/purchase"
}

/*

/*
    let merchantUrl: String = "https://staging-api.deema.me/api/merchant/v1/purchase"
    func createMerchantOrder(merchantOrderId : String , amount : String , currency : String,  onCompletion: @escaping (_ _response : ResponseModel ,  _ isSuccess: Bool ) -> Void) {
        showHud()
        let params = [
            "merchant_order_id" : merchantOrderId ,
            "amount" : amount ,
            "currency_code" : currency
        ]
        let headers = [ "source" : "sdk",
                     "Authorization" : "Basic sk_test_d5gntxxdoRNGkAweKjWZMr8iocXd3oNO1Wz5VJuW_65"
        ]

        APIManager.post(urlString: merchantUrl , withParams:params, andHeaders:headers ) { responseModel in
            let isSuccess = responseModel.isSuccess
            onCompletion(responseModel  ,isSuccess)
        }
    }

 */

 */