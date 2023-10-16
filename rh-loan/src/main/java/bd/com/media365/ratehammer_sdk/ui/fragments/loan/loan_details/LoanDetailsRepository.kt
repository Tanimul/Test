package bd.com.media365.ratehammer_sdk.ui.fragments.loan.loan_details

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.select_application.response.SelectApplication
import bd.com.media365.ratehammer_sdk.network.api.LoanApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoanDetailsRepository @Inject constructor(
    private val loanApi: LoanApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

    //<editor-fold desc="Network Call">
    fun applicationSelect(
        applicationId: String,
        offerId: String
    ): Flow<Resource<SelectApplication>> {

        return networkCall(connectivityManager) {
            loanApi.applicationSelect(
                applicationId,offerId
            )
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>
}