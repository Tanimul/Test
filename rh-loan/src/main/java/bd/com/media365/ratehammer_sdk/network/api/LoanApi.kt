package bd.com.media365.ratehammer_sdk.network.api

import androidx.annotation.Keep
import bd.com.media365.ratehammer_sdk.constants.ApiConstants
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.request.ApplicationStoreRequest
import bd.com.media365.ratehammer_sdk.models.fields.response.Fields
import bd.com.media365.ratehammer_sdk.models.profile.response.Profile
import bd.com.media365.ratehammer_sdk.models.select_application.response.SelectApplication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


@Keep
interface LoanApi {

    @POST(ApiConstants.API_APPLICATIONS_STORE)
    suspend fun storeApplication(@Body applicationStoreRequest: ApplicationStoreRequest): Response<ApplicationStore>

    @GET(ApiConstants.API_FIELDS)
    suspend fun getFields(): Response<Fields>

    @POST("${ApiConstants.API_APPLICATIONS}/{application_id}/${ApiConstants.API_OFFERS}/{offer_id}/${ApiConstants.API_SELECT}")
    suspend fun applicationSelect(@Path("application_id") applicationId: String,
                                  @Path("offer_id") offerId: String): Response<SelectApplication>

    @GET(ApiConstants.API_PROFILE)
    suspend fun getUserProfile(): Response<Profile>

}