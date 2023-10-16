package bd.com.media365.ratehammer_sdk.di

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.Keep
import bd.com.media365.ratehammer_sdk.network.core.MyServiceInterceptor
import bd.com.media365.ratehammer_sdk.utils.DataStorePrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named


@Module
@InstallIn(ActivityRetainedComponent::class)
@Keep
object SdkActivityModule {

    @Provides
    fun provideNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    @SdkOkHttpClient
    @Provides
    fun provideNetworkInterceptor(
        interceptor: MyServiceInterceptor
    ): OkHttpClient {
        val build = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES).addInterceptor(interceptor)

        //if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        build.addInterceptor(httpLoggingInterceptor)
        //}

        return build.build()
    }


    @SdkRetrofit
    @Provides
    fun provideRetrofitInstance(
        @Named("sdk_base_url") baseUrl: String,
        @SdkOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideHeaderInterceptor(dataStorePrefUtils: DataStorePrefUtils): MyServiceInterceptor {
        return MyServiceInterceptor(dataStorePrefUtils)
    }

}