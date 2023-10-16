package bd.com.media365.ratehammer_sdk.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SdkConnectivityManager

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SdkRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SdkOkHttpClient