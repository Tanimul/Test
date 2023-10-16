package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.family_residence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request.FamilyResidenceStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.response.FamilyResidenceStore
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
class FamilyResidenceViewModel @Inject constructor(
    private val repository: FamilyResidenceRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    //<editor-fold desc="Localization Variable Declaration">

    // Use Flow<String> instead of LiveData<String>
    var title = flow {
        emit(localizationUtil.getLocalizedText(R.string.lender_required))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var yes = flow {
        emit(localizationUtil.getLocalizedText(R.string.yes))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var no = flow {
        emit(localizationUtil.getLocalizedText(R.string.no))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var familyBreadWinner = flow {
        emit(localizationUtil.getLocalizedText(R.string.family_breadwinner))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var dependentsPrivateSchool = flow {
        emit(localizationUtil.getLocalizedText(R.string.number_dependents_private_school))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var dependentsPublicSchool = flow {
        emit(localizationUtil.getLocalizedText(R.string.number_dependents_public_school))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var numberChildren = flow {
        emit(localizationUtil.getLocalizedText(R.string.number_of_children))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var numberDomesticWorker = flow {
        emit(localizationUtil.getLocalizedText(R.string.number_of_domestic_workers))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var houseType = flow {
        emit(localizationUtil.getLocalizedText(R.string.house_type))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var owned = flow {
        emit(localizationUtil.getLocalizedText(R.string.owned))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var rental = flow {
        emit(localizationUtil.getLocalizedText(R.string.rental))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var companyProvided = flow {
        emit(localizationUtil.getLocalizedText(R.string.company_provided))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var residentialType = flow {
        emit(localizationUtil.getLocalizedText(R.string.residential_type))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var apartment = flow {
        emit(localizationUtil.getLocalizedText(R.string.apartment))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var villa = flow {
        emit(localizationUtil.getLocalizedText(R.string.villa))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var duplex = flow {
        emit(localizationUtil.getLocalizedText(R.string.duplex))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var zero = flow {
        emit(localizationUtil.getLocalizedText(R.string._0))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var proceedNext = flow {
        emit(localizationUtil.getLocalizedText(R.string.proceed_next))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //</editor-fold>

    //<editor-fold desc="Network Call">
    fun storeFamilyResidences(
      familyResidenceStoreRequest: FamilyResidenceStoreRequest
    ): Flow<Resource<FamilyResidenceStore>> {
        Timber.d("storeFamilyResidences:")

        return repository.storeFamilyResidences(familyResidenceStoreRequest).flowOn(Dispatchers.IO)

    }
    //</editor-fold>

}