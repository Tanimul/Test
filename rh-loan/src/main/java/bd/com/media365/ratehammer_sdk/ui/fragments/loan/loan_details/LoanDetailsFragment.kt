package bd.com.media365.ratehammer_sdk.ui.fragments.loan.loan_details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.forEachIndexed
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.adapter.AdapterOfferDetails
import bd.com.media365.ratehammer_sdk.base.BaseFragment
import bd.com.media365.ratehammer_sdk.common.viewmodels.DataStoreViewModel
import bd.com.media365.ratehammer_sdk.common.viewmodels.SharedViewModel
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkLoanDetailsBinding
import bd.com.media365.ratehammer_sdk.extention.toast
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ModelApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.response.Offers
import bd.com.media365.ratehammer_sdk.network.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoanDetailsFragment : BaseFragment<FragmentSdkLoanDetailsBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val loanDetailsViewModel: LoanDetailsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by hiltNavGraphViewModels(R.id.flow_nav_graph_loan)
    private lateinit var adapterOfferDetails: AdapterOfferDetails
    private lateinit var mOfferItems: ArrayList<Offers>
    private lateinit var mApplicationStoreItems: ArrayList<ModelApplicationStore>

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSdkLoanDetailsBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_sdk_loan_details, container, false)

    override fun init() {
        Timber.d("init")

        binding.viewModel = loanDetailsViewModel
        binding.lifecycleOwner = this // Ensure LiveData updates are observed

        initLoadingDialog()
        initView()
        handleMirroring()

        binding.layoutSdkToolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {

        //Access the object
        val applicationStoreInfo = sharedViewModel.applicationStoreInfo
        val offersInfo = sharedViewModel.offersInfo
        val position = sharedViewModel.position
        val maxTotalPayable = sharedViewModel.maxTotalPayable
        val maxMonthlyPayment = sharedViewModel.maxMonthlyPayment
        val maxLikelyArp = sharedViewModel.maxLikelyArp

        mApplicationStoreItems = ArrayList()
        mOfferItems = ArrayList()

        adapterOfferDetails = AdapterOfferDetails(
            offersInfo, applicationStoreInfo, maxTotalPayable,
            maxMonthlyPayment,
            maxLikelyArp
        ) { offerId ->
            sharedViewModel.offerId = "$offerId"
            selectApplication(sharedViewModel.applicationId, sharedViewModel.offerId)
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        with(binding.rvOffer) {

            setHasFixedSize(true)
            this.layoutManager = layoutManager
            adapter = adapterOfferDetails

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val currentItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    updateDotIndicator(currentItem)
                }
            })
        }

        PagerSnapHelper().attachToRecyclerView(binding.rvOffer)

        // Scroll to the initial position
        scrollToPosition(position)

        // Initialize the dot indicator with the number of items in the adapter.
        setupDotIndicator(adapterOfferDetails.itemCount)

    }

    private fun setupDotIndicator(itemCount: Int) {

        val dotLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(4, 0, 4, 0)
        }

        val dotSelector = R.drawable.offer_dot_selector // Use a selector for dot appearance.
        val context = requireContext()

        repeat(itemCount) {
            val dotView = ImageView(context).apply {
                setImageResource(dotSelector)
                layoutParams = dotLayoutParams
            }
            binding.dotIndicatorLayout.addView(dotView)
        }
    }

    private fun updateDotIndicator(position: Int) {
        binding.dotIndicatorLayout.forEachIndexed { index, dotView ->
            (dotView as? ImageView)?.isSelected = index == position // Highlight the selected dot.
        }
    }

    private fun handleMirroring() {
        lifecycleScope.launch {
            val localeCode =
                dataStoreViewModel.getStringValue(AppConstants.DataStorePref.LOCALE).first()
            Timber.d("locale: $localeCode")
            if (localeCode == "ar") {
                binding.layoutSdkToolbar.ivPath1648.scaleX = -1f
            }
        }
    }

    private fun scrollToPosition(position: Int) {
        // Ensure the position is valid
        if (position in 0 until adapterOfferDetails.itemCount) {
            binding.rvOffer.scrollToPosition(position)
            updateDotIndicator(position)
        }
    }

    private fun selectApplication(
        applicationId: String, offerId: String

    ) {
        Timber.d("selectApplication: $applicationId - $offerId")
        lifecycleScope.launch {
            loanDetailsViewModel.applicationSelect(
                applicationId, offerId
            ).collect() {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                        Timber.d("selectApplication: ${it.message}")
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
                        Timber.d("selectApplication: $data")
                        sharedViewModel.selectedOffer = data?.selectedOffer

                        findNavController().navigate(R.id.action_loanDetailsFragmentSdk_to_PEPFragmentSdk)
                    }
                }
            }
        }
    }


}