package bd.com.media365.ratehammer_sdk.ui.fragments.loan.new_loan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.request.ApplicationStoreRequest
import bd.com.media365.ratehammer_sdk.models.profile.response.Profile
import bd.com.media365.ratehammer_sdk.models.select_application.response.SelectApplication
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewLoanViewModel @Inject constructor(
    private val repository: NewLoanRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    //<editor-fold desc="Localization Variable Declaration">

    // Use Flow<String> instead of LiveData<String>
    var title = flow {
        emit(localizationUtil.getLocalizedText(R.string.new_lender_quote))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var progressLenderTitle = flow {
        emit(localizationUtil.getLocalizedText(R.string.title))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var progressLenderLowest = flow {
        emit(localizationUtil.getLocalizedText(R.string.lowest))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var progressLenderValue = flow {
        emit(localizationUtil.getLocalizedText(R.string._00))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var tapSeeLenderDetails = flow {
        emit(localizationUtil.getLocalizedText(R.string.tap_see_lender_details))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //</editor-fold>

    //<editor-fold desc="Network Call">
    fun storeApplication(
        storeApplicationRequest: ApplicationStoreRequest
    ): Flow<Resource<ApplicationStore>> {
        Timber.d("storeApplication:")

        return repository.storeApplication(storeApplicationRequest).flowOn(Dispatchers.IO)

    }

    fun applicationSelect(
        applicationId: String,
        offerId: String
    ): Flow<Resource<SelectApplication>> {

        Timber.d("storeApplication:")

        return repository.applicationSelect(applicationId,offerId).flowOn(Dispatchers.IO)
    }
    fun getUserProfile(
    ): Flow<Resource<Profile>> {
        Timber.d("getUserProfile:")

        return repository.getUserProfile().flowOn(Dispatchers.IO)

    }


    //</editor-fold>

}