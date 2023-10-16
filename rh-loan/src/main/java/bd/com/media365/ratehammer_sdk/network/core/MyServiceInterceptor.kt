package bd.com.media365.ratehammer_sdk.network.core

import androidx.annotation.Keep
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import bd.com.media365.ratehammer_sdk.extention.checkIsEmpty
import bd.com.media365.ratehammer_sdk.utils.DataStorePrefUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


@Keep
class MyServiceInterceptor @Inject constructor(private val dataStorePrefUtils: DataStorePrefUtils) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val locale: String
        val sessionToken: String
        runBlocking {
            locale =
                dataStorePrefUtils.getStringValue(AppConstants.DataStorePref.LOCALE).first()
                    .toString()
            sessionToken =
                dataStorePrefUtils.getStringValue(AppConstants.DataStorePref.SESSION_TOKEN).first()
                    .toString()

        }

        Timber.d("intercept: $locale")
        Timber.d("intercept: $sessionToken")

        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        // needs credentials
        requestBuilder
            .addHeader(AppConstants.API_ACCEPT, AppConstants.API_ACCEPT_VALUE)
            .addHeader(AppConstants.API_PLATFORM, AppConstants.API_PLATFORM_VALUE)
            .addHeader(
                AppConstants.DataStorePref.LOCALE,
                locale
            )

        if (!sessionToken.checkIsEmpty()) {
            requestBuilder.addHeader(AppConstants.API_AUTHORIZATION, "Bearer $sessionToken")
        }

        requestBuilder.method(request.method, request.body)

        /*if (BuildConfig.DEBUG) {
             chain.proceed(request)
         }*/
        return chain.proceed(requestBuilder.build())
    }

}