package bd.com.media365.ratehammer_sdk.models.fields.response

import com.google.gson.annotations.SerializedName


data class Fields(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var modelFields: ModelFields? = ModelFields()

)
