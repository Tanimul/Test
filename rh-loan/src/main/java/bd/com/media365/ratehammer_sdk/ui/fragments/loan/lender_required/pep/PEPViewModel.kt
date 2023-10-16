package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.pep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.response.PepInfoStore
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.request.PepInfoStoreRequest
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.utils.HintColorHelper.createSpannableTextWithRedAsterisk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PEPViewModel @Inject constructor(
    private val repository: PEPRepository,
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

    var doYouHaveARelationshipToAPepIndividual = flow {
        emit(localizationUtil.getLocalizedText(R.string.do_you_have_a_relationship_to_a_pep_individual))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var isAnyRelativesKnownAsPep = flow {
        emit(localizationUtil.getLocalizedText(R.string.is_any_relatives_known_as_pep))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var haveYouHeldAnyPoliticalPosition = flow {
        emit(localizationUtil.getLocalizedText(R.string.have_you_held_any_political_position))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var pleaseSpecifyThePosition = flow {
        emit(localizationUtil.getLocalizedText(R.string.please_specify_the_position))
    }.map { text -> createSpannableTextWithRedAsterisk(text) }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var enterThePosition = flow {
        emit(localizationUtil.getLocalizedText(R.string.enter_the_position))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var proceedNext = flow {
        emit(localizationUtil.getLocalizedText(R.string.proceed_next))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //</editor-fold>

    //<editor-fold desc="Network Call">
    fun storePepInfo(
        pepInfoStoreRequest: PepInfoStoreRequest
    ): Flow<Resource<PepInfoStore>> {
        Timber.d("storePepInfo:")

        return repository.storePepInfo(pepInfoStoreRequest).flowOn(Dispatchers.IO)

    }
    //</editor-fold>
}