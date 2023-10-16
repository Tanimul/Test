package bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response

import com.google.gson.annotations.SerializedName


data class ResidenceInfo(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("house_type") var houseType: String? = null,
    @SerializedName("residential_type") var residentialType: String? = null

)