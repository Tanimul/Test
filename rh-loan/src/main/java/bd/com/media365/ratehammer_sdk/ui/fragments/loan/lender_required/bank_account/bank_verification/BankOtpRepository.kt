package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.bank_account.bank_verification

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.request.OtpRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.response.BankOTP
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.response.OtpVerify
import bd.com.media365.ratehammer_sdk.network.api.LoanRequiredApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BankOtpRepository @Inject constructor(
    private val loanRequiredApi: LoanRequiredApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager,
) {

    //<editor-fold desc="Network Call">
    fun getOtpResponse(
        bankAccountId: String
    ): Flow<Resource<BankOTP>> {

        return networkCall(connectivityManager) {
            loanRequiredApi.getBankOTP(bankAccountId)
        }.flowOn(Dispatchers.IO)
    }

    fun getBankOtpVerifyResponse(
        bankAccountId: String,otp: String
    ): Flow<Resource<OtpVerify>> {
        return networkCall(connectivityManager) {
            loanRequiredApi.getBankOtpVerify(bankAccountId,OtpRequest(otp = otp))
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}

