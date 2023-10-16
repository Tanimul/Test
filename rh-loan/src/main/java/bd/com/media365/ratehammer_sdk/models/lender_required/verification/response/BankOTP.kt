package bd.com.media365.ratehammer_sdk.models.lender_required.verification.response

import com.google.gson.annotations.SerializedName


data class BankOTP(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)