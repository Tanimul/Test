package bd.com.media365.ratehammer_sdk.models.lender_required.verification.request

import com.google.gson.annotations.SerializedName

data class OtpRequest(
    @SerializedName("otp") var otp: String? = null
)