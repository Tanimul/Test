package bd.com.media365.ratehammer_sdk.models.lender_required.verification.response

import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("otp") var otp: String? = null

)