package bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.response

import com.google.gson.annotations.SerializedName


data class Bank(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("logo") var logo: Logo? = Logo()

)