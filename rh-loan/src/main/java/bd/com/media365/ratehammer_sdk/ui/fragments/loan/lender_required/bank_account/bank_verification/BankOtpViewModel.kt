package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.bank_account.bank_verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.response.BankOTP
import bd.com.media365.ratehammer_sdk.models.lender_required.verification.response.OtpVerify
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BankOtpViewModel @Inject constructor(
    private val repository: BankOtpRepository,
    private val localizationUtil: LocalizationUtil
) :
    ViewModel() {

    //<editor-fold desc="Localization Variable Declaration">
    // Use Flow<String> instead of LiveData<String>
    val title = flow {
        emit(localizationUtil.getLocalizedText(R.string.verification))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val enterCode = flow {
        emit(localizationUtil.getLocalizedText(R.string.enter_code))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val weSentACodeToTheMobileNumber = flow {
        emit(localizationUtil.getLocalizedText(R.string.enter_the_6_digit_code_sent_via_sms))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _numberFlow: MutableStateFlow<String> = MutableStateFlow("")
    val number = _numberFlow.asStateFlow()

    fun updateMobileNumber(mobileNumber: String) {
        _numberFlow.value = "$mobileNumber"
    }

    val requestCodeAgainIn = flow {
        emit(localizationUtil.getLocalizedText(R.string.request_code_again_in))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val resentTimeCount = flow {
        emit(localizationUtil.getLocalizedText(R.string.resent_time_count))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val confirmCode = flow {
        emit(localizationUtil.getLocalizedText(R.string.confirm_code))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    // </editor-fold>

    //<editor-fold desc="Network Call">
    fun getBankOtpResponse(
        bankAccountId: String
    ): Flow<Resource<BankOTP>> {
        Timber.d("getBankOtpResponse:$bankAccountId")

        return repository.getOtpResponse(bankAccountId).flowOn(Dispatchers.IO)

    }

    fun getBankOtpVerifyResponse(
        bankAccountId: String, otp: String
    ): Flow<Resource<OtpVerify>> {
        Timber.d("getBankOtpVerifyResponse: $otp")

        return repository.getBankOtpVerifyResponse(bankAccountId, otp).flowOn(Dispatchers.IO)

    }
    //</editor-fold>
}