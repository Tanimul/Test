package bd.com.media365.ratehammer_sdk.models.select_application.response

import com.google.gson.annotations.SerializedName


data class SelectApplication(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)