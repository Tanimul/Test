package bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response

import com.google.gson.annotations.SerializedName


data class FamilyInfo(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_family_breadwinner") var isFamilyBreadwinner: Int? = null,
    @SerializedName("no_of_dependent_in_private_school") var noOfDependentInPrivateSchool: Int? = null,
    @SerializedName("no_of_dependent_in_public_school") var noOfDependentInPublicSchool: Int? = null,
    @SerializedName("no_of_children") var noOfChildren: Int? = null,
    @SerializedName("no_of_domestic_workers") var noOfDomesticWorkers: Int? = null

)