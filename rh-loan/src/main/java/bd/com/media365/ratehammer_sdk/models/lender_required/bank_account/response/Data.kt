package bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.response


import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("account_name") var accountName: String? = null,
    @SerializedName("iban_number") var ibanNumber: String? = null,
    @SerializedName("verified_at") var verifiedAt: String? = null,
    @SerializedName("bank") var bank: Bank? = Bank()

)