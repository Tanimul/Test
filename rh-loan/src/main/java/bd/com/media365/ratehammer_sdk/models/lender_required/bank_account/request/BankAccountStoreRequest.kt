package bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.request

import com.google.gson.annotations.SerializedName

data class BankAccountStoreRequest(
    @SerializedName("application_id") var applicationId: String? = null,
    @SerializedName("bank_id") var bankId: Int? = null,
    @SerializedName("account_name") var accountName: String? = null,
    @SerializedName("iban_number") var ibanNumber: String? = null,
    @SerializedName("swift_code") var swiftCode: String? = null,
    @SerializedName("is_default") var isDefault: Boolean? = null

)