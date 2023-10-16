package bd.com.media365.ratehammer_sdk.models.banks.response

import com.google.gson.annotations.SerializedName


data class Pagination(

    @SerializedName("current_page") var currentPage: Int? = null,
    @SerializedName("per_page") var perPage: Int? = null,
    @SerializedName("current_page_count") var currentPageCount: Int? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("last_page") var lastPage: Int? = null

)