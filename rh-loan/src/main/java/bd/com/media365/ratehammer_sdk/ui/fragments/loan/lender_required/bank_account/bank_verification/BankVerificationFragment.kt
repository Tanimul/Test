package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.bank_account.bank_verification

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.base.BaseFragment
import bd.com.media365.ratehammer_sdk.common.viewmodels.DataStoreViewModel
import bd.com.media365.ratehammer_sdk.common.viewmodels.SharedViewModel
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkBankAccountBinding
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkBankVerficationBinding
import bd.com.media365.ratehammer_sdk.extention.toast
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class BankVerificationFragment : BaseFragment<FragmentSdkBankVerficationBinding>() {
    private lateinit var countDownTimer: CountDownTimer
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val bankOtpViewModel: BankOtpViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by hiltNavGraphViewModels(R.id.flow_nav_graph_loan)
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSdkBankVerficationBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_sdk_bank_verfication, container, false)

    override fun init() {
        Timber.d("init")

        binding.viewModel = bankOtpViewModel
        binding.lifecycleOwner = this // Ensure LiveData updates are observed

        initView()
        handleMirroring()
        initLoadingDialog()

        binding.btnConfirmCode.setOnClickListener {
            verifyOtp(binding.pvBankCode.text.toString())
        }

        binding.tvRequestCodeAgain.setOnClickListener {
            initView()
        }

        binding.layoutCustomToolbar.ibBack.setOnClickListener {
        findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        binding.pvBankCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.count() == 6) {
                    verifyOtp(binding.pvBankCode.text.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button press event within the fragment
            // You can perform any custom logic here.

            // If you want to navigate back to a specific destination:
            findNavController().popBackStack()

            // If you want to consume the back button press event:
            //isEnabled = false
        }


    }


    private fun verifyOtp(otp: String) {
        Timber.d("otp: $otp")
        lifecycleScope.launch {
            bankOtpViewModel.getBankOtpVerifyResponse(sharedViewModel.bankAccountId,otp).collect() {
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


                    }
                }
            }
        }
    }

    private fun initView() {
        binding.tvResendTimeCount.visibility = View.VISIBLE
        binding.tvRequestCodeAgain.isEnabled = false
        binding.tvRequestCodeAgain.setTextColor(requireContext().getColor(R.color.color_334155))
        resendCountDown()
        otpApiResponse()

        lifecycleScope.launch {
            val countryCallingCode =
                dataStoreViewModel.getStringValue(AppConstants.DataStorePref.COUNTRY_CALLING_CODE).first()
            val number =
                dataStoreViewModel.getStringValue(AppConstants.DataStorePref.CONTACT_NO).first()
            bankOtpViewModel.updateMobileNumber("$countryCallingCode $number")
        }

    }

    private fun resendCountDown() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var remaining: String = if (millisUntilFinished / 1000 < 10) {
                    "00 : 0${millisUntilFinished / 1000}"
                } else {
                    "00 : ${millisUntilFinished / 1000}"
                }
                binding.tvResendTimeCount.text = remaining
            }

            override fun onFinish() {
                binding.tvRequestCodeAgain.isEnabled = true
                binding.tvResendTimeCount.visibility = View.GONE
                binding.tvRequestCodeAgain.text = resources.getString(R.string.request_code_again)
                binding.tvRequestCodeAgain.setTextColor(requireContext().getColor(R.color.color_1ED2E3))

            }
        }

        countDownTimer.start()
    }

    private fun otpApiResponse(
    ) {
        lifecycleScope.launch {
            bankOtpViewModel.getBankOtpResponse(sharedViewModel.bankAccountId).collect() {
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
                        val otp = it.data?.data?.otp
                        fillBankCode(otp)
                    }
                }
            }
        }
    }

    private fun fillBankCode(otp: String?) {
//        val editablePinCode = Editable.Factory.getInstance().newEditable(otp)
//        binding.pvAbsherCode.text = editablePinCode

        binding.tvCodeDemo.text = "Otp is: $otp"
    }

    private fun handleMirroring() {
        lifecycleScope.launch {
            val localeCode =
                dataStoreViewModel.getStringValue(AppConstants.DataStorePref.LOCALE).first()
            Timber.d("locale: $localeCode")
            if (localeCode != "en") {
                binding.layoutCustomToolbar.ivPath1648.scaleX = -1f
            }
        }
    }


}