package bd.com.media365.ratehammer_sdk.models.fields.response

import com.google.gson.annotations.SerializedName


data class LoanTerms(

    @SerializedName("name") var name: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("min_loan") var minLoan: String? = null,
    @SerializedName("max_loan") var maxLoan: String? = null

)