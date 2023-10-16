package bd.com.media365.ratehammer_sdk.models.fields.response

import com.google.gson.annotations.SerializedName


data class Genders(

    @SerializedName("name") var name: String? = null,
    @SerializedName("value") var value: String? = null

)