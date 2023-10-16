package bd.com.media365.ratehammer_sdk.common.repository

import android.net.ConnectivityManager
import bd.com.media365.ratehammer_sdk.di.SdkConnectivityManager
import javax.inject.Inject

class LenderRequiredRepository @Inject constructor(
    @SdkConnectivityManager private val connectivityManager: ConnectivityManager
) {

}