package bd.com.media365.ratehammer_sdk.models.banks.response

import com.google.gson.annotations.SerializedName


data class Banks(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("meta") var meta: Meta? = Meta()

)