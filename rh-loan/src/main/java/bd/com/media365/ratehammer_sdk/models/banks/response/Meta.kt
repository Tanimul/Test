package bd.com.media365.ratehammer_sdk.models.banks.response

import com.google.gson.annotations.SerializedName


data class Meta(

    @SerializedName("pagination") var pagination: Pagination? = Pagination()

)