package bd.com.media365.ratehammer_sdk.models.select_application.response

import com.google.gson.annotations.SerializedName


data class SelectedOffer(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("lender") var lender: Lender? = Lender()

)