package bd.com.media365.ratehammer_sdk.dialogs.select_bank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.banks.response.Banks
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.utils.HintColorHelper.createSpannableTextWithRedAsterisk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectBankViewModel @Inject constructor(
    private val repository: SelectBankRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    //<editor-fold desc="Flow Declaration">
    private var _banksStateFlow: MutableStateFlow<Resource<Banks>> =
        MutableStateFlow(Resource.Loading())
    val getBanks: StateFlow<Resource<Banks>> = _banksStateFlow
    //</editor-fold>

    init {
        // Launch a coroutine to collect the network call result and update the Flow
        viewModelScope.launch {
            repository.getBanks().collect { result ->
                _banksStateFlow.value = result

            }
        }
    }

    //<editor-fold desc="Localization Variable Declaration">

    // Use Flow<String> instead of LiveData<String>
    val selectYourPreferredBank = flow {
        emit(localizationUtil.getLocalizedText(R.string.select_your_preferred_bank))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val asPerSamaRegistration = flow {
        emit(localizationUtil.getLocalizedText(R.string.as_per_sama_s_registration))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val selectBank = flow {
        emit(localizationUtil.getLocalizedText(R.string.select_bank))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    //</editor-fold>

}
