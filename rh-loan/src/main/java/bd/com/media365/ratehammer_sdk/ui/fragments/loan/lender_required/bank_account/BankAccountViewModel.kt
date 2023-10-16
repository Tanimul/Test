package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.bank_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.request.BankAccountStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.response.BankAccountStoreResponse
import bd.com.media365.ratehammer_sdk.network.core.Resource
import bd.com.media365.ratehammer_sdk.utils.HintColorHelper
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
class BankAccountViewModel @Inject constructor(
    private val repository: BankAccountRepository,
    private val localizationUtil: LocalizationUtil
) : ViewModel() {

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

    var selectBank = flow {
        emit(localizationUtil.getLocalizedText(R.string.select_bank))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var accountInfo = flow {
        emit(localizationUtil.getLocalizedText(R.string.account_info))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var accountHolderName = flow {
        emit(localizationUtil.getLocalizedText(R.string.accountHolderName))
    }.map { text -> HintColorHelper.createSpannableTextWithRedAsterisk(text) }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var swiftBIC = flow {
        emit(localizationUtil.getLocalizedText(R.string.swift_bic))
    }.map { text -> HintColorHelper.createSpannableTextWithRedAsterisk(text) }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var ibanNumber = flow {
        emit(localizationUtil.getLocalizedText(R.string.iban_number))
    }.map { text -> HintColorHelper.createSpannableTextWithRedAsterisk(text) }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var makeDefaultAccount = flow {
        emit(localizationUtil.getLocalizedText(R.string.make_default_account))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var noteOnlyTransfers = flow {
        emit(localizationUtil.getLocalizedText(R.string.note_only_transfers))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var submit = flow {
        emit(localizationUtil.getLocalizedText(R.string.submit))
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //</editor-fold>

    //<editor-fold desc="Network Call">
    fun storeBankAccounts(
        bankAccountStoreRequest: BankAccountStoreRequest
    ): Flow<Resource<BankAccountStoreResponse>> {
        Timber.d("storeBankAccounts:")

        return repository.storeBankAccounts(bankAccountStoreRequest).flowOn(Dispatchers.IO)

    }
    //</editor-fold>

}