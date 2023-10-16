package bd.com.media365.ratehammer_sdk.models.applications_store.response


import com.google.gson.annotations.SerializedName


data class ModelApplicationStore(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("loan_amount") var loanAmount: Int? = null,
    @SerializedName("loan_term_in_month") var loanTermInMonth: Int? = null,
    @SerializedName("loan_purpose") var loanPurpose: LoanPurpose? = LoanPurpose(),
    @SerializedName("status") var status: String? = null,
    @SerializedName("offers") var offers: ArrayList<Offers> = arrayListOf()

)