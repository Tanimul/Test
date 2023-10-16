package bd.com.media365.ratehammer_sdk.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.Keep
import bd.com.media365.ratehammer_sdk.utils.DataStorePrefUtils
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Keep
object SdkModule {
    @Provides
    @Named("sdk_base_url")
    fun providesBaseUrl(): String {
        return AppConstants.API_URL_DEV
    }

    @SdkConnectivityManager
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideDataStorePrefUtils(@ApplicationContext context: Context): DataStorePrefUtils {
        return DataStorePrefUtils(context)
    }

}