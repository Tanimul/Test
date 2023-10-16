package bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response

import com.google.gson.annotations.SerializedName


data class FamilyResidenceStore(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)