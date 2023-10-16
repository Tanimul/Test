package bd.com.media365.ratehammer_sdk.network.api

import androidx.annotation.Keep
import bd.com.media365.ratehammer_sdk.constants.ApiConstants
import bd.com.media365.ratehammer_sdk.models.banks.response.Banks
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.request.BankAccountStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.response.BankAccountStoreResponse
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request.FamilyResidenceStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response.FamilyResidenceStore
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.response.PepInfoStore
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.request.PepInfoStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.request.OtpRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.response.BankOTP
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.response.OtpVerify
import bd.com.media365.ratehammer_sdk.models.select_application.response.SelectApplication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


@Keep
interface LoanRequiredApi {

    @POST(ApiConstants.API_PEP_INFOS_STORE)
    suspend fun storePepInfo(@Body pepInfoStoreRequest: PepInfoStoreRequest): Response<PepInfoStore>

    @POST(ApiConstants.API_FAMILY_RESIDENCES_STORE)
    suspend fun storeFamilyResidences(@Body familyResidenceStoreRequest: FamilyResidenceStoreRequest): Response<FamilyResidenceStore>

    @POST(ApiConstants.API_BANK_ACCOUNTS_STORE)
    suspend fun storeBankAccounts(@Body bankAccountStoreRequest: BankAccountStoreRequest): Response<BankAccountStoreResponse>

    @GET(ApiConstants.API_BANKS)
    suspend fun getBanks(): Response<Banks>

    @GET("${ApiConstants.API_BANK_ACCOUNT}/{bank_account_id}/${ApiConstants.API_OTP_SEND}")
    suspend fun getBankOTP(@Path("bank_account_id") bankAccountId: String): Response<BankOTP>

    @POST("${ApiConstants.API_BANK_ACCOUNT}/{bank_account_id}/${ApiConstants.API_BANK_OTP_VERIFY}")
    suspend fun getBankOtpVerify(@Path("bank_account_id")bankAccountId: String,@Body otpRequest: OtpRequest): Response<OtpVerify>


}