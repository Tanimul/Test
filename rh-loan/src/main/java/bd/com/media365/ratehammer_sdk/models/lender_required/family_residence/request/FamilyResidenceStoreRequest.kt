package bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request


import com.google.gson.annotations.SerializedName


data class FamilyResidenceStoreRequest(

    @SerializedName("application_id") var applicationId: String? = null,
    @SerializedName("family_info") var familyInfo: FamilyInfo? = FamilyInfo(),
    @SerializedName("residence_info") var residenceInfo: ResidenceInfo? = ResidenceInfo()

)