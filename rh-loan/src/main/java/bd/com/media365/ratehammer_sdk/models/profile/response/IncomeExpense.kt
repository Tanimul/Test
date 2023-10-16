package bd.com.media365.ratehammer_sdk.models.profile.response

import com.google.gson.annotations.SerializedName


data class IncomeExpense(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("monthly_net_income") var monthlyNetIncome: Int? = null,
    @SerializedName("monthly_credit_commitment") var monthlyCreditCommitment: Int? = null,
    @SerializedName("monthly_mortgage") var monthlyMortgage: Int? = null,
    @SerializedName("other_monthly_expense") var otherMonthlyExpense: Int? = null

)