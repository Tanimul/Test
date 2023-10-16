package bd.com.media365.ratehammer_sdk.models.lender_required.pep.response

import com.google.gson.annotations.SerializedName

data class PepInfoStore(
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()
)