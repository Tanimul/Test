package bd.com.media365.ratehammer_sdk.models.applications_store.response

import com.google.gson.annotations.SerializedName


data class ApplicationStore(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var modelApplicationStore: ModelApplicationStore? = ModelApplicationStore()

)