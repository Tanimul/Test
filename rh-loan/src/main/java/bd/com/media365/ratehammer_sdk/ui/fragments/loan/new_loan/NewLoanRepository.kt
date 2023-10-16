package bd.com.media365.ratehammer_sdk.ui.fragments.loan.new_loan

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.applications_store.request.ApplicationStoreRequest
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ApplicationStore
import bd.com.media365.ratehammer_sdk.models.profile.response.Profile
import bd.com.media365.ratehammer_sdk.models.select_application.response.SelectApplication
import bd.com.media365.ratehammer_sdk.network.api.LoanApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewLoanRepository @Inject constructor(
    private val loanApi: LoanApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

    //<editor-fold desc="Network Call">
    fun storeApplication(
        applicationStoreRequest: ApplicationStoreRequest
    ): Flow<Resource<ApplicationStore>> {

        return networkCall(connectivityManager) {
            loanApi.storeApplication(
                applicationStoreRequest
            )
        }.flowOn(Dispatchers.IO)
    }

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

    fun getUserProfile(
    ): Flow<Resource<Profile>> {

        return networkCall(connectivityManager) {
            loanApi.getUserProfile()
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}