package bd.com.media365.ratehammer_sdk.models.profile.response

import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("user") var user: User? = User(),
    @SerializedName("bank_accounts") var bankAccounts: ArrayList<BankAccounts> = arrayListOf(),
    @SerializedName("income_expense") var incomeExpense: IncomeExpense? = IncomeExpense(),
    @SerializedName("pep_info") var pepInfo: PepInfo? = PepInfo()

)