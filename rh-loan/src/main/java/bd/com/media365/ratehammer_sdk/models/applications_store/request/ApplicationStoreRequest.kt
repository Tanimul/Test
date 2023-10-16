package bd.com.media365.ratehammer_sdk.models.applications_store.request

import com.google.gson.annotations.SerializedName


data class ApplicationStoreRequest(

    @SerializedName("loan_term_in_month") var loanTermInMonth: String? = null,
    @SerializedName("loan_amount") var loanAmount: String? = null,
    @SerializedName("loan_purpose_id") var loanPurposeId: Int? = null

)