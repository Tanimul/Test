package bd.com.media365.ratehammer_sdk.models.fields.response

import com.google.gson.annotations.SerializedName


data class LoanAmounts(

    @SerializedName("min") var min: String? = null,
    @SerializedName("max") var max: String? = null

)