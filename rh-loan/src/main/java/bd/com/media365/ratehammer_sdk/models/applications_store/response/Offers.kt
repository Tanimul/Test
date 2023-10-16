package bd.com.media365.ratehammer_sdk.models.applications_store.response

import com.google.gson.annotations.SerializedName


data class Offers(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("interest_rate") var interestRate: String? = null,
    @SerializedName("total_interest") var totalInterest: String? = null,
    @SerializedName("total_payable") var totalPayable: String? = null,
    @SerializedName("monthly_payment") var monthlyPayment: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("lender") var lender: Lender? = Lender()

)