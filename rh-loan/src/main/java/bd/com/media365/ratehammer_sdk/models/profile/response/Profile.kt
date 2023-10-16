package bd.com.media365.ratehammer_sdk.models.profile.response

import com.google.gson.annotations.SerializedName


data class Profile(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)