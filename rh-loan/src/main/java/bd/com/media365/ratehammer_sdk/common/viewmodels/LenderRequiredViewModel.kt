package bd.com.media365.ratehammer_sdk.common.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.common.repository.LenderRequiredRepository
import bd.com.media365.ratehammer_sdk.extention.getLocalizedTextFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LenderRequiredViewModel @Inject constructor(
    private val repository: LenderRequiredRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    private val _view01 = MutableStateFlow<Drawable?>(null)
    private val _view02 = MutableStateFlow<Drawable?>(null)
    private val _view03 = MutableStateFlow<Drawable?>(null)

    // Expose these as StateFlows
    val view01: StateFlow<Drawable?> = _view01.asStateFlow()
    val view02: StateFlow<Drawable?> = _view02.asStateFlow()
    val view03: StateFlow<Drawable?> = _view03.asStateFlow()

    fun updateView(index: Int, drawable: Drawable?) {
        when(index){
            0 ->  _view01.value = drawable
            1 ->  _view02.value = drawable
            2 ->  _view03.value = drawable
        }
    }


    private val _logoFlow: MutableStateFlow<String> = MutableStateFlow("")
    val logo: StateFlow<String> = _logoFlow.asStateFlow()
    fun updateLogo(url: String) {
        _logoFlow.value = url
    }

    private val _StepFlow: MutableStateFlow<String> = MutableStateFlow("")
    val step: StateFlow<String> = _StepFlow.asStateFlow()
    fun updateStep(stepID: Int) {
        viewModelScope.launch {
            localizationUtil.getLocalizedTextFlow(stepID, viewModelScope).collect {
                _StepFlow.value = "$it"
            }
        }
    }


    private val _stepTitleFlow: MutableStateFlow<String> = MutableStateFlow("")
    val stepTitle: StateFlow<String> = _stepTitleFlow.asStateFlow()
    fun updateStepTitle(stepTitleID: Int) {
        viewModelScope.launch {
            localizationUtil.getLocalizedTextFlow(stepTitleID, viewModelScope).collect {
                _stepTitleFlow.value = "$it"
            }
        }
    }

    // Use Flow<String> instead of LiveData<String>
    val title = localizationUtil.getLocalizedTextFlow(R.string.lender_required, viewModelScope)
    val essentialInformationForLenders = localizationUtil.getLocalizedTextFlow(
        R.string.essential_information_for_lenders,
        viewModelScope
    )
    val ensureAccurateAndTruthfulInformation = localizationUtil.getLocalizedTextFlow(
        R.string.ensure_accurate_and_truthful_information,
        viewModelScope
    )

}
