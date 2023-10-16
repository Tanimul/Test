package bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response

import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("family_info") var familyInfo: FamilyInfo? = FamilyInfo(),
    @SerializedName("residence_info") var residenceInfo: ResidenceInfo? = ResidenceInfo()

)