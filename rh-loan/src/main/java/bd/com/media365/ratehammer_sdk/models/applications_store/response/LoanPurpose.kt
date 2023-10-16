package bd.com.media365.ratehammer_sdk.models.applications_store.response

import com.google.gson.annotations.SerializedName


data class LoanPurpose(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("description") var description: String? = null

)