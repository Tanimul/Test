package bd.com.media365.ratehammer_sdk.ui.fragments.loan.loan_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
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
class LoanDetailsViewModel @Inject constructor(
    private val repository: LoanDetailsRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    //<editor-fold desc="Localization Variable Declaration">

    // Use Flow<String> instead of LiveData<String>
    var title = flow {
        emit(localizationUtil.getLocalizedText(R.string.loan_details))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //</editor-fold>

    //<editor-fold desc="Network Call">
    fun applicationSelect(
        applicationId: String,
        offerId: String
    ): Flow<Resource<SelectApplication>> {

        Timber.d("storeApplication:")

        return repository.applicationSelect(applicationId,offerId).flowOn(Dispatchers.IO)
    }
    //</editor-fold>

}