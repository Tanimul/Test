package bd.com.media365.ratehammer_sdk.dialogs.choose_loan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.fields.response.Fields
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.utils.HintColorHelper.createSpannableTextWithRedAsterisk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseYourLoanViewModel @Inject constructor(
    private val repository: ChooseYourLoanRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    //<editor-fold desc="Flow Declaration">
    private var _fieldsStateFlow: MutableStateFlow<Resource<Fields>> =
        MutableStateFlow(Resource.Loading())
    private val _loanAmountValueFlow: MutableStateFlow<String> = MutableStateFlow("0.0")
    private val _seekBarMaxValueFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    val getFields: StateFlow<Resource<Fields>> = _fieldsStateFlow
    val loanAmountValue: StateFlow<String> = _loanAmountValueFlow.asStateFlow()
    val seekBarMaxValue: StateFlow<Int> = _seekBarMaxValueFlow.asStateFlow()
    //</editor-fold>

    //<editor-fold desc="Network Call Init">

    // Update loanAmountValue when you receive the value
    fun updateLoanAmountValue(newValue: String) {
        _loanAmountValueFlow.value = newValue
    }

    init {
        // Launch a coroutine to collect the network call result and update the Flow
        viewModelScope.launch {
            repository.getField().collect { result ->
                _fieldsStateFlow.value = result

                // Update loanAmountValue if the result is successful
                updateLoanAmountValue(result.data?.modelFields?.loanAmounts?.min ?: "0.0")

                _seekBarMaxValueFlow.value =
                    result.data?.modelFields?.loanAmounts?.max?.toDouble()?.toInt() ?: 0

            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Localization Variable Declaration">

    // Use Flow<String> instead of LiveData<String>
    val chooseYourLoan = flow {
        emit(localizationUtil.getLocalizedText(R.string.choose_your_loan))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val loanAmount = flow {
        emit(localizationUtil.getLocalizedText(R.string.loan_amount))
    }.map { text -> createSpannableTextWithRedAsterisk(text) }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val sar = flow {
        emit(localizationUtil.getLocalizedText(R.string.sar))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val howToUse = flow {
        emit(localizationUtil.getLocalizedText(R.string.how_to_use))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val toBeginSimplyCustomiseTheSlider = flow {
        emit(localizationUtil.getLocalizedText(R.string.to_begin_simply_customise_the_slider))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val chooseLoanPurpose = flow {
        emit(localizationUtil.getLocalizedText(R.string.choose_loan_purpose))
    }.map { text -> createSpannableTextWithRedAsterisk(text) }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val chooseLoanTerm = flow {
        emit(localizationUtil.getLocalizedText(R.string.choose_loan_term))
    }.map { text -> createSpannableTextWithRedAsterisk(text) }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val iMReady = flow {
        emit(localizationUtil.getLocalizedText(R.string.i_m_ready))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //</editor-fold>

}
