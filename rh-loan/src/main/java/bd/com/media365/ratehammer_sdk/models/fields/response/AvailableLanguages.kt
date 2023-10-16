package bd.com.media365.ratehammer_sdk.models.fields.response

import com.google.gson.annotations.SerializedName


data class AvailableLanguages(

    @SerializedName("name") var name: String? = null,
    @SerializedName("name_in_lang") var nameInLang: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("direction") var direction: String? = null

)