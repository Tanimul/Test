package bd.com.media365.ratehammer_sdk.di.modules

import bd.com.media365.ratehammer_sdk.di.SdkRetrofit
import bd.com.media365.ratehammer_sdk.network.api.LoanApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class LoanModule {
    @Provides
    fun provideLoanApi(@SdkRetrofit retrofit: Retrofit): LoanApi {
        return retrofit.create(LoanApi::class.java)
    }

}