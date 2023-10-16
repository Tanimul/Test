package bd.com.media365.ratehammer_sdk.models.lender_required.pep.request

import com.google.gson.annotations.SerializedName


data class PepInfoStoreRequest(
    @SerializedName("application_id") var applicationId: String? = null,
    @SerializedName("has_relation_to_pep") var hasRelationToPep: Int? = null,
    @SerializedName("has_relative_known_as_pep") var hasRelativeKnownAsPep: Int? = null,
    @SerializedName("has_any_political_position_held_last_10_years") var hasAnyPoliticalPositionHeldLast10Years: Int? = null,
    @SerializedName("political_position_name") var politicalPositionName: String? = null

)