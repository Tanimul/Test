package bd.com.media365.ratehammer_sdk.dialogs.choose_loan

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import bd.com.media365.ratehammer_sdk.models.fields.response.Fields
import bd.com.media365.ratehammer_sdk.network.api.LoanApi
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.network.core.networkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChooseYourLoanRepository @Inject constructor(
    private val loanApi: LoanApi,
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

    //<editor-fold desc="Network Call">
    fun getField(
    ): Flow<Resource<Fields>> {

        return networkCall(connectivityManager) {
            loanApi.getFields()
        }.flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}