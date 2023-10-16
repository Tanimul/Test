package bd.com.media365.ratehammer_sdk.models.select_application.response

import com.google.gson.annotations.SerializedName


data class Lender(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("logo") var logo: Logo? = Logo()

)