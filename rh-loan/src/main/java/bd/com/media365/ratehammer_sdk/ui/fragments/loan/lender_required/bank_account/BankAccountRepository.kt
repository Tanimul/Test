package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.bank_account

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.request.BankAccountStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.response.BankAccountStoreResponse
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request.FamilyResidenceStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response.FamilyResidenceStore
import bd.com.media365.ratehammer_sdk.network.api.LoanRequiredApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BankAccountRepository @Inject constructor(
    private val loanRequiredApi: LoanRequiredApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

    //<editor-fold desc="Network Call">
    fun storeBankAccounts(
    bankAccountStoreRequest: BankAccountStoreRequest
    ): Flow<Resource<BankAccountStoreResponse>> {

        return networkCall(connectivityManager) {
            loanRequiredApi.storeBankAccounts(
                bankAccountStoreRequest
            )
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}