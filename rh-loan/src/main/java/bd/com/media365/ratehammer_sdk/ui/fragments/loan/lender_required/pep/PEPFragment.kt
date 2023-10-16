package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.pep

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
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
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkPepBinding
import bd.com.media365.ratehammer_sdk.extention.toast
import bd.com.media365.ratehammer_sdk.models.lender_required.pep.request.PepInfoStoreRequest
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PEPFragment : BaseFragment<FragmentSdkPepBinding>() {

    //<editor-fold desc="Variable Declaration">
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by hiltNavGraphViewModels(R.id.flow_nav_graph_loan)
    private val pepViewModel: PEPViewModel by viewModels()
    private val lenderRequiredViewModel: LenderRequiredViewModel by viewModels()

    private var hasRelationToPep: Int? = null
    private var hasRelativeKnownAsPep: Int? = null
    private var hasAnyPoliticalPositionHeldLast10Years: Int? = null
    private var politicalPositionName: String? = null
    //</editor-fold>

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSdkPepBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_sdk_pep, container, false)

    //<editor-fold desc="Init">
    override fun init() {
        Timber.d("init")

        binding.viewModel = pepViewModel
        binding.lenderRequiredViewModel = lenderRequiredViewModel
        binding.lifecycleOwner = this // Ensure LiveData updates are observed

        initLoadingDialog()
        initView()
        initListener()
        buttonBackgroundUpdate()

        binding.layoutLenderRequiredToolbar.layoutSdkToolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnProceedNext.setOnClickListener {
            storePepInfo(
                hasRelationToPep,
                hasRelativeKnownAsPep,
                hasAnyPoliticalPositionHeldLast10Years,
                politicalPositionName
            )
        }

    }

    private fun initView() {

        lenderRequiredViewModel.apply {
            updateView(
                0,
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_pager_dot)
            )
            updateView(
                1,
                ContextCompat.getDrawable(requireContext(), R.drawable.unselected_pager_dot)
            )
            updateView(
                2,
                ContextCompat.getDrawable(requireContext(), R.drawable.unselected_pager_dot)
            )

            updateLogo(sharedViewModel.selectedOffer?.lender?.logo?.src.toString())
            updateStep(R.string.step_1_3)
            updateStepTitle(R.string.pep_information)
        }

        binding.layoutHeldAnyPoliticalPosition.divider.visibility = View.INVISIBLE

    }
    //</editor-fold>

    //<editor-fold desc="Listener">
    private fun initListener() {

        initRadioGroupListener(
            binding.layoutRelationshipToAPepIndividual.radioGroupChoosing,
            { hasRelationToPep = 1 },
            { hasRelationToPep = 0 }
        )

        initRadioGroupListener(
            binding.layoutRelativesKnownAsPep.radioGroupChoosing,
            { hasRelativeKnownAsPep = 1 },
            { hasRelativeKnownAsPep = 0 }
        )

        initRadioGroupListener(
            binding.layoutHeldAnyPoliticalPosition.radioGroupChoosing,
            {
                binding.tvPleaseSpecifyPosition.visibility = View.VISIBLE
                binding.layoutEnterThePosition.visibility = View.VISIBLE
                hasAnyPoliticalPositionHeldLast10Years = 1
                politicalPositionName = null
            },
            {
                binding.tvPleaseSpecifyPosition.visibility = View.GONE
                binding.layoutEnterThePosition.visibility = View.GONE
                hasAnyPoliticalPositionHeldLast10Years = 0
                politicalPositionName = ""
            }
        )

        setupTextWatcher(binding.etPosition) { politicalPositionName = it }
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

    private fun initRadioGroupListener(
        radioGroup: RadioGroup,
        onYesSelected: () -> Unit,
        onNoSelected: () -> Unit
    ) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btn_yes -> onYesSelected()
                R.id.btn_no -> onNoSelected()
            }
            buttonBackgroundUpdate()
        }
    }
    //</editor-fold>

    //<editor-fold desc="Button Background Update by Fields">
    private fun buttonBackgroundUpdate() {
        val isValidFields = isValidFields()

        binding.btnProceedNext.apply {
            isEnabled = isValidFields
            background = ContextCompat.getDrawable(context, if (isValidFields) R.drawable.ripple_theme_gradient_curve else R.drawable.background_button)
        }
    }

    private fun isValidFields(): Boolean {
        return hasRelationToPep != null && hasRelationToPep != null && hasAnyPoliticalPositionHeldLast10Years != null && politicalPositionName != null
    }
    //</editor-fold>

    //<editor-fold desc="Network Call">
    private fun storePepInfo(
        hasRelationToPep: Int?,
        hasRelativeKnownAsPep: Int?,
        hasAnyPoliticalPositionHeldLast10Years: Int?,
        politicalPositionName: String?
    ) {
        Timber.d("storePepInfo: $hasRelationToPep - $hasRelativeKnownAsPep - $hasAnyPoliticalPositionHeldLast10Years -$politicalPositionName")
        lifecycleScope.launch {
            pepViewModel.storePepInfo(
                PepInfoStoreRequest(
                    sharedViewModel.applicationId,
                    hasRelationToPep,
                    hasRelativeKnownAsPep,
                    hasAnyPoliticalPositionHeldLast10Years,
                    politicalPositionName
                )
            ).collect() {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                        Timber.d("storePepInfo: ${it.message}")
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
                        Timber.d("storePepInfo: $data")
                        findNavController().navigate(R.id.action_PEPFragmentSdk_to_familyResidenceFragmentSdk)
                    }
                }
            }
        }
    }
    //</editor-fold>

}