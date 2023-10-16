package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.bank_account

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.base.BaseFragment
import bd.com.media365.ratehammer_sdk.common.viewmodels.DataStoreViewModel
import bd.com.media365.ratehammer_sdk.common.viewmodels.LenderRequiredViewModel
import bd.com.media365.ratehammer_sdk.common.viewmodels.SharedViewModel
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkBankAccountBinding
import bd.com.media365.ratehammer_sdk.dialogs.select_bank.SelectBankDialog
import bd.com.media365.ratehammer_sdk.dialogs.select_bank.SelectBankViewModel
import bd.com.media365.ratehammer_sdk.extention.isValidateIBAN
import bd.com.media365.ratehammer_sdk.extention.toast
import bd.com.media365.ratehammer_sdk.models.banks.response.Data
import bd.com.media365.ratehammer_sdk.models.lender_required.bank_account.request.BankAccountStoreRequest
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BankAccountFragment : BaseFragment<FragmentSdkBankAccountBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by hiltNavGraphViewModels(R.id.flow_nav_graph_loan)
    private val bankAccountViewModel: BankAccountViewModel by viewModels()
    private val lenderRequiredViewModel: LenderRequiredViewModel by viewModels()
    private val selectBankViewModel: SelectBankViewModel by viewModels()

    private var selectBankDialog: SelectBankDialog? = null

    var bankId: Int? = null
    var accountName: String? = null
    var ibanNumber: String? = null
    var swiftCode: String? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSdkBankAccountBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_sdk_bank_account, container, false)

    override fun init() {
        Timber.d("init")

        binding.lenderRequiredViewModel = lenderRequiredViewModel
        binding.viewModel = bankAccountViewModel
        binding.lifecycleOwner = this // Ensure LiveData updates are observed

        initLoadingDialog()
        initView()
        initListener()
        buttonBackgroundUpdate()
        getBanks()

        binding.layoutLenderRequiredToolbar.layoutSdkToolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutSelectedBank.ibSwap.setOnClickListener {

            selectBankDialog?.let {
                it.show()
            } ?: getBanks()
        }

        binding.btnSubmit.setOnClickListener {
            storeBankInfo(
                accountName,
                ibanNumber,
                swiftCode,
                bankId,
                binding.ivCheckBox.isChecked
            )
        }
    }

    private fun initListener() {
        setupTextWatcher(binding.etAccountHolderName) { accountName = it }
        setupTextWatcher(binding.etSwiftBIC) { swiftCode = it }
        setupTextWatcher(binding.etIbanNumber) { ibanNumber = it?.takeIf { it.isValidateIBAN() } }
    }

    private fun setupTextWatcher(editText: EditText, onTextChanged: (String?) -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s?.toString().takeIf { count > 0 })
                buttonBackgroundUpdate()
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })
    }

    private fun storeBankInfo(
        accountName: String?,
        ibanNumber: String?,
        swiftCode: String?,
        bankId: Int?,
        isDefault: Boolean?
    ) {
        Timber.d("storeBankInfo: $accountName - $ibanNumber - $swiftCode - $bankId -$isDefault")
        lifecycleScope.launch {
            bankAccountViewModel.storeBankAccounts(
                BankAccountStoreRequest(
                    sharedViewModel.applicationId,
                    bankId, accountName, ibanNumber, swiftCode, isDefault
                )
            ).collect() {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                        Timber.d("storeBankInfo: ${it.message}")
                    }

                    is Resource.Loading -> {
                        showLoadingDialog()
                    }

                    is Resource.NetworkError -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                    }

                    is Resource.SessionExpired -> {
                        dismissLoadingDialog()
                    }

                    is Resource.Success -> {
                        dismissLoadingDialog()
                        val data = it.data?.data
                        sharedViewModel.bankAccountId = "${data?.id}"
                        Timber.d("storeBankInfo: $data")
                        findNavController().navigate(R.id.action_bankAccountFragmentSdk_to_bankVerificationFragment)
                    }
                }
            }
        }
    }


    private fun initView() {

        lenderRequiredViewModel.apply {
            updateView(
                0, ContextCompat.getDrawable(requireContext(), R.drawable.unselected_pager_dot)
            )
            updateView(
                1, ContextCompat.getDrawable(requireContext(), R.drawable.unselected_pager_dot)
            )
            updateView(
                2, ContextCompat.getDrawable(requireContext(), R.drawable.selected_pager_dot)
            )

            updateLogo(sharedViewModel.selectedOffer?.lender?.logo?.src.toString())
            updateStep(R.string.step_3_3)
            updateStepTitle(R.string.bank_account)

        }


    }

    private fun getBanks() {
        lifecycleScope.launch {
            selectBankViewModel.getBanks.collect {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                    }

                    is Resource.Loading -> {
                        showLoadingDialog()
                    }

                    is Resource.NetworkError -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                    }

                    is Resource.SessionExpired -> {
                        dismissLoadingDialog()
                    }

                    is Resource.Success -> {
                        dismissLoadingDialog()
                        val data = it.data?.data
                        initSelectBankDialog(data)

                    }

                    else -> {}
                }
            }
        }
    }

    private fun initSelectBankDialog(data: ArrayList<Data>?) {

        lifecycleScope.launch {
            if (selectBankDialog == null) {
                selectBankDialog = SelectBankDialog(
                    viewLifecycleOwner, selectBankViewModel,
                    data,
                    requireContext(),
                    acceptClick = { data ->
                        binding.layoutBank.visibility=View.VISIBLE
                        binding.selectedBank = data
                        bankId=data.id
                    },
                    cancelClick = {

                    }
                )
            }
            selectBankDialog?.show()
        }
    }

    private fun buttonBackgroundUpdate() {
        val isValidFields = isValidFields()

        binding.btnSubmit.apply {
            isEnabled = isValidFields
            background = ContextCompat.getDrawable(context, if (isValidFields) R.drawable.ripple_theme_gradient_curve else R.drawable.background_button)
        }
    }

    private fun isValidFields(): Boolean {
        return accountName != null && swiftCode != null && ibanNumber != null && bankId != null
    }
}