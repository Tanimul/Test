package bd.com.media365.ratehammer_sdk.di.modules

import bd.com.media365.ratehammer_sdk.di.SdkRetrofit
import bd.com.media365.ratehammer_sdk.network.api.LoanApi
import bd.com.media365.ratehammer_sdk.network.api.LoanRequiredApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class LoanRequiredModule {
    @Provides
    fun provideLoanRequiredApi(@SdkRetrofit retrofit: Retrofit): LoanRequiredApi {
        return retrofit.create(LoanRequiredApi::class.java)
    }

}