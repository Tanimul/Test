package bd.com.media365.ratehammer_sdk.dialogs.select_bank

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.banks.response.Banks
import bd.com.media365.ratehammer_sdk.network.api.LoanRequiredApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SelectBankRepository @Inject constructor(
    private val loanRequiredApi: LoanRequiredApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

    //<editor-fold desc="Network Call">
    fun getBanks(
    ): Flow<Resource<Banks>> {

        return networkCall(connectivityManager) {
            loanRequiredApi.getBanks()
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}