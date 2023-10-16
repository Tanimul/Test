package bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.response

import com.google.gson.annotations.SerializedName


data class BankAccountStoreResponse(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)