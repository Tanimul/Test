package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.pep

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.response.PepInfoStore
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.request.PepInfoStoreRequest
import bd.com.media365.ratehammer_sdk.network.api.LoanRequiredApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PEPRepository @Inject constructor(
    private val loanRequiredApi: LoanRequiredApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

    //<editor-fold desc="Network Call">
    fun storePepInfo(
        pepInfoStoreRequest: PepInfoStoreRequest
    ): Flow<Resource<PepInfoStore>> {

        return networkCall(connectivityManager) {
            loanRequiredApi.storePepInfo(
                pepInfoStoreRequest
            )
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}