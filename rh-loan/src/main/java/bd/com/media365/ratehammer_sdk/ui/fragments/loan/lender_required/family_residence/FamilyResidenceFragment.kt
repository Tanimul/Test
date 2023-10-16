package bd.com.media365.ratehammer_sdk.ui.fragments.loan.lender_required.family_residence
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
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
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkFamilyResidenceBinding
import bd.com.media365.ratehammer_sdk.databinding.ItemSdkFamilyResidenceInfoBinding
import bd.com.media365.ratehammer_sdk.extention.toast
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request.FamilyInfo
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request.FamilyResidenceStoreRequest
import bd.com.media365.ratehammer_sdk.models.lender_required.family_residence.request.ResidenceInfo
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class FamilyResidenceFragment : BaseFragment<FragmentSdkFamilyResidenceBinding>() {

    //<editor-fold desc="Variable Declaration">
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by hiltNavGraphViewModels(R.id.flow_nav_graph_loan)
    private val familyResidenceViewModel: FamilyResidenceViewModel by viewModels()
    private val lenderRequiredViewModel: LenderRequiredViewModel by viewModels()

    private var isFamilyBreadwinner: Int? = null
    private var noOfDependentInPrivateSchool: Int? = 0
    private var noOfDependentInPublicSchool: Int? = 0
    private var noOfChildren: Int? = 0
    private var noOfDomesticWorkers: Int? = 0
    private var houseType: String? = null
    private var residentialType: String? = null
    //</editor-fold>

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSdkFamilyResidenceBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_sdk_family_residence, container, false)

    //<editor-fold desc="Init">
    override fun init() {
        Timber.d("init")

        binding.viewModel = familyResidenceViewModel
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
            storeFamilyResidence(
                isFamilyBreadwinner,
                noOfDependentInPrivateSchool,
                noOfDependentInPublicSchool,
                noOfChildren,
                noOfDomesticWorkers,
                houseType,
                residentialType
            )
        }

    }

    private fun initView() {

        lenderRequiredViewModel.apply {
            updateView(
                0, ContextCompat.getDrawable(requireContext(), R.drawable.unselected_pager_dot)
            )
            updateView(
                1, ContextCompat.getDrawable(requireContext(), R.drawable.selected_pager_dot)
            )
            updateView(
                2, ContextCompat.getDrawable(requireContext(), R.drawable.unselected_pager_dot)
            )

            updateLogo(sharedViewModel.selectedOffer?.lender?.logo?.src.toString())
            updateStep(R.string.step_2_3)
            updateStepTitle(R.string.family_residence_information)

        }

    }
    //</editor-fold>

    //<editor-fold desc="Listener">
    private fun initListener() {

        initRadioGroupListener(
            binding.layoutFamilyBreadWinner.radioGroupChoosing,
            { isFamilyBreadwinner = 1 },
            { isFamilyBreadwinner = 0 }
        )

        setupRadioGroupListener(
            binding.radioGroupHouseType,
            R.id.btn_owned to "owned",
            R.id.btn_rental to "rental",
            R.id.btn_companyProvided to "company_provided"
        ) { houseType = it }

        setupRadioGroupListener(
            binding.radioGroupResidentialType,
            R.id.btn_apartment to "apartment",
            R.id.btn_villa to "villa",
            R.id.btn_duplex to "duplex"
        ) { residentialType = it }

        setupIncrementDecrementListener(
            binding.layoutDependentsPrivateSchool,
            binding.layoutDependentsPrivateSchool.tvValue
        ) { noOfDependentInPrivateSchool = it }

        setupIncrementDecrementListener(
            binding.layoutDependentsPublicSchool,
            binding.layoutDependentsPublicSchool.tvValue
        ) { noOfDependentInPublicSchool = it }

        setupIncrementDecrementListener(
            binding.layoutNumberChildren,
            binding.layoutNumberChildren.tvValue
        ) { noOfChildren = it }

        setupIncrementDecrementListener(
            binding.layoutNumberDomesticWorker,
            binding.layoutNumberDomesticWorker.tvValue
        ) { noOfDomesticWorkers = it }
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

    private fun setupRadioGroupListener(
        radioGroup: RadioGroup,
        vararg options: Pair<Int, String>,
        action: (String?) -> Unit
    ) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedOption = options.firstOrNull { it.first == checkedId }?.second
            action(selectedOption)
            buttonBackgroundUpdate()
        }
    }


    private fun setupIncrementDecrementListener(
        layout: ItemSdkFamilyResidenceInfoBinding,
        textView: TextView,
        action: (Int) -> Unit
    ) {

        layout.ibPlus.setOnClickListener {
            val currentValue = textView.text.toString().toInt()
            val newValue = currentValue.plus(1)
            textView.text = "$newValue"
            action.invoke(newValue)
        }

        layout.ibMinus.setOnClickListener {
            val currentValue = textView.text.toString().toInt()
            if (currentValue > 0) {
                val newValue = currentValue.minus(1)
                textView.text = "$newValue"
                action.invoke(newValue)
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Button Background Update by Fields">
    private fun buttonBackgroundUpdate() {
        val isValidFields = isValidFields()

        binding.btnProceedNext.apply {
            isEnabled = isValidFields
            background = ContextCompat.getDrawable(
                context,
                if (isValidFields) R.drawable.ripple_theme_gradient_curve else R.drawable.background_button
            )
        }
    }

    private fun isValidFields(): Boolean {
        return isFamilyBreadwinner != null && houseType != null && residentialType != null
    }
    //</editor-fold>

    //<editor-fold desc="Network Call">
    private fun storeFamilyResidence(
        isFamilyBreadwinner: Int?,
        noOfDependentInPrivateSchool: Int?,
        noOfDependentInPublicSchool: Int?,
        noOfChildren: Int?,
        noOfDomesticWorkers: Int?,
        houseType: String?,
        residentialType: String?
    ) {
        Timber.d("storeFamilyResidence: $isFamilyBreadwinner - $noOfDependentInPrivateSchool - $noOfDependentInPublicSchool - $noOfChildren - $noOfDomesticWorkers - $houseType - $residentialType ")
        lifecycleScope.launch {
            familyResidenceViewModel.storeFamilyResidences(
                FamilyResidenceStoreRequest(
                    sharedViewModel.applicationId,
                    FamilyInfo(
                        isFamilyBreadwinner,
                        noOfDependentInPrivateSchool,
                        noOfDependentInPublicSchool,
                        noOfChildren,noOfDomesticWorkers
                    ), ResidenceInfo(
                        houseType,
                        residentialType
                    )
                )
            ).collect() {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                        Timber.d("storeFamilyResidence: ${it.message}")
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
                        Timber.d("storeFamilyResidence: $data")
                        findNavController().navigate(R.id.action_familyResidenceFragmentSdk_to_bankAccountFragmentSdk)
                    }
                }
            }
        }
    }
    //</editor-fold>

}
