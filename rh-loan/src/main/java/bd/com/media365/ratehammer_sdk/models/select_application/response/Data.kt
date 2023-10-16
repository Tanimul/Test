package bd.com.media365.ratehammer_sdk.models.select_application.response

import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("loan_amount") var loanAmount: Int? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("loan_term_in_month") var loanTermInMonth: Int? = null,
    @SerializedName("paid_out_at") var paidOutAt: String? = null,
    @SerializedName("loan_maturity_date") var loanMaturityDate: String? = null,
    @SerializedName("selected_offer") var selectedOffer: SelectedOffer? = SelectedOffer(),
    @SerializedName("payment") var payment: String? = null,
    @SerializedName("status") var status: String? = null

)