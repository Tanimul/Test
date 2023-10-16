package bd.com.media365.ratehammer_sdk.ui.fragments.loan.new_loan

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.adapter.AdapterOffer
import bd.com.media365.ratehammer_sdk.base.BaseFragment
import bd.com.media365.ratehammer_sdk.common.viewmodels.DataStoreViewModel
import bd.com.media365.ratehammer_sdk.common.viewmodels.SharedViewModel
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import bd.com.media365.ratehammer_sdk.databinding.FragmentSdkNewLoanBinding
import bd.com.media365.ratehammer_sdk.dialogs.choose_loan.ChooseYourLoanDialog
import bd.com.media365.ratehammer_sdk.dialogs.choose_loan.ChooseYourLoanViewModel
import bd.com.media365.ratehammer_sdk.extention.convertDpToPx
import bd.com.media365.ratehammer_sdk.extention.getDisplayWidth
import bd.com.media365.ratehammer_sdk.extention.toast
import bd.com.media365.ratehammer_sdk.models.applications_store.request.ApplicationStoreRequest
import bd.com.media365.ratehammer_sdk.models.applications_store.response.ModelApplicationStore
import bd.com.media365.ratehammer_sdk.models.applications_store.response.Offers
import bd.com.media365.ratehammer_sdk.models.fields.response.LoanAmounts
import bd.com.media365.ratehammer_sdk.models.fields.response.ModelFields
import bd.com.media365.ratehammer_sdk.models.profile.response.User
import bd.com.media365.ratehammer_sdk.network.core.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat


@AndroidEntryPoint
class NewLoanFragment : BaseFragment<FragmentSdkNewLoanBinding>() {

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val newLoanViewModel: NewLoanViewModel by viewModels()
    private val chooseYourLoanViewModel: ChooseYourLoanViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by hiltNavGraphViewModels(R.id.flow_nav_graph_loan)

    private var adapterOffer: AdapterOffer? = null
    private var mOfferItems: ArrayList<Offers> = ArrayList()
    private var mOfferItemsAll: ArrayList<Offers> = ArrayList()
    private var radarValue: ArrayList<RadarModel> = ArrayList()

    private var _loanAmount: Int = 0
    private var _loanMonth: Int = 0
    private var _loanPurposeID: Int = 0

    var minLoanAmount: Int = 0

    private var loanAmounts: LoanAmounts? = null
    private var responseModelApplicationStore: ModelApplicationStore? = null

    private val formatter = DecimalFormat("#,###,###")

    private var maxTotalPayable: Double = 0.0
    private var maxMonthlyPayment: Double = 0.0
    private var maxLikelyArp: Double = 0.0

    private var isChooseYourLoanDialogShowed = false
    private var imageUrl: String? = ""

    var centerImage: Drawable? = null
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSdkNewLoanBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_sdk_new_loan, container, false)

    override fun init() {
        Timber.d("init")

        binding.viewModel = newLoanViewModel
        binding.lifecycleOwner = this // Ensure LiveData updates are observed

        initLoadingDialog()
        handleMirroring()
        initSeekBarListener()
        initView()
        getUserProfile()

        binding.layoutSdkToolbar.ibBack.setOnClickListener {
            activity?.finish()
        }

        binding.btnChooseThisLender.setOnClickListener {
            selectApplication(sharedViewModel.applicationId, sharedViewModel.offerId)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )


    }

    private fun getUserProfile() {
        lifecycleScope.launch {
            newLoanViewModel.getUserProfile(
            ).collect() {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                        Timber.d("getUserProfile: ${it.message}")
                    }

                    is Resource.Loading -> {
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
                        val userInfo = it.data?.data?.user
                        if (userInfo != null) {
                            saveUser(userInfo)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun saveUser(user: User) {
        lifecycleScope.launch {

            dataStoreViewModel.saveToDataStore(AppConstants.DataStorePref.ID, "${user.id}")
            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.FIRST_NAME,
                "${user.firstName}"
            )
            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.LAST_NAME,
                "${user.lastName}"
            )
            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.EMAIL,
                "${user.email}"
            )

            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.CONTACT_NO,
                "${user.contactNo}"
            )
            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.COUNTRY_CALLING_CODE,
                "${user.countryCallingCode}"
            )
            dataStoreViewModel.saveToDataStore(AppConstants.DataStorePref.GENDER, "${user.gender}")
            dataStoreViewModel.saveToDataStore(AppConstants.DataStorePref.DOB, "${user.dob}")
            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.NATIONAL_ID,
                "${user.nationalId}"
            )

            dataStoreViewModel.saveToDataStore(
                AppConstants.DataStorePref.IMAGE_URL,
                "${user.profilePhoto?.src}"
            )


            imageUrl = user.profilePhoto?.src

            imageUrl?.let {
                loadImageFromUrl(it) { drawable ->
                    centerImage = drawable
                }
            }

        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            // Handle the back button press event within the fragment
            // You can perform any custom logic here.

            // If you want to navigate back to a specific destination:
            activity?.finish()

            // If you want to consume the back button press event:
            //isEnabled = false
        }
    }

    //<editor-fold desc="Radder view">
    private fun initRadder() {
        Timber.d("initRadder")
        // Clear the FrameLayout before adding a new view
        binding.flRadder.removeAllViews()

        binding.gifUniverse.visibility = View.INVISIBLE
        binding.layoutTopBar.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE

        val circleDrawingView = RadarView(requireActivity(), null)

        // Get the screen width in pixels
        val displayWidth = requireContext().getDisplayWidth()

        // Convert dp to pixels based on screen density
        val marginPixels = requireContext().convertDpToPx(16)

        // Customize circle properties
        circleDrawingView.outerCircleRadius = (displayWidth.toFloat() / 2f) - marginPixels
        circleDrawingView.gapBetweenCircles = 30f
        circleDrawingView.totalCircle = 4

        // Set the image resource
//        circleDrawingView.centerImage =
//            ContextCompat.getDrawable(requireActivity(), R.drawable.avatar)

        circleDrawingView.centerImage = centerImage


        if (radarValue != null) {
            circleDrawingView.radarValue = radarValue!!
        }

        // Set the CircleDrawingView to the frame layout
        binding.flRadder.addView(circleDrawingView)
    }
    //</editor-fold>

    private fun initSeekBarListener() {
        Timber.d("initSeekBarListener")

        val stepSize = 500
        binding.sbLoanAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {


                val minValue = minLoanAmount
                // Ensure that progress is updated in steps of 100
                val adjustedProgress =
                    if (progress < minValue) minValue else (progress / stepSize) * stepSize

                seekBar.progress = if (minValue == adjustedProgress) 0 else adjustedProgress
                updateHeader(adjustedProgress, _loanMonth)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                _loanAmount = if (seekBar.progress < minLoanAmount) {
                    minLoanAmount
                } else {
                    seekBar.progress
                }
                resetValues()
                initRadder()
                binding.gifUniverse.visibility = View.VISIBLE
                storeApplication(
                    _loanMonth.toString(),
                    _loanPurposeID,
                    _loanAmount.toString()
                )

            }
        })

        binding.sbLoanTerms.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                updateHeader(_loanAmount, progress)
                binding.sbLoanTerms.progress = progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                _loanMonth = seekBar.progress
                resetValues()
                initRadder()
                binding.gifUniverse.visibility = View.VISIBLE
                storeApplication(
                    _loanMonth.toString(),
                    _loanPurposeID,
                    _loanAmount.toString()
                )
            }
        })
    }

    private fun initChooseYourLoanDialog(modelFields: ModelFields?) {
        lifecycleScope.launch {
            if (modelFields != null) {
                loanAmounts = modelFields.loanAmounts
                val loanTerms = modelFields.loanTerms
                val loanPurpose = modelFields.loanPurposes
                val chooseYourLoanDialog = ChooseYourLoanDialog(
                    viewLifecycleOwner, chooseYourLoanViewModel,
                    loanTerms,
                    loanPurpose,
                    loanAmounts,
                    requireContext(),
                    acceptClick = { loanTermInMonth, loanPurposeID, loanAmount ->
                        _loanAmount = loanAmount.toDouble().toInt()
                        _loanMonth = loanTermInMonth.toDouble().toInt()

                        initRadder()
                        updateSeekBar(loanAmounts)
                        updateHeader(_loanAmount, _loanMonth)

                        binding.gifUniverse.visibility = View.VISIBLE


                        storeApplication(
                            loanTermInMonth,
                            loanPurposeID,
                            loanAmount
                        )
                        _loanPurposeID = loanPurposeID

                        isChooseYourLoanDialogShowed = true
                    },
                    cancelClick = {
                        activity?.finish()
                    })

                chooseYourLoanDialog.show()
            }

        }
    }

    private fun storeApplication(
        loanTermInMonth: String?, loanPurposeID: Int?, loanAmount: String
    ) {
        Timber.d("storeApplication: $loanTermInMonth - $loanPurposeID - $loanAmount")
        lifecycleScope.launch {
            newLoanViewModel.storeApplication(
                ApplicationStoreRequest(
                    loanTermInMonth, loanAmount, loanPurposeID
                )
            ).collect() {
                when (it) {
                    is Resource.Error -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                        Timber.d("storeApplication: ${it.message}")
                    }

                    is Resource.Loading -> {
                    }

                    is Resource.NetworkError -> {
                        dismissLoadingDialog()
                        activity?.toast("${it.message}")
                    }

                    is Resource.SessionExpired -> {
                        dismissLoadingDialog()
                    }

                    is Resource.Success -> {
                        responseModelApplicationStore = it.data?.modelApplicationStore

                        sharedViewModel.applicationId = "${responseModelApplicationStore?.id}"
                        sharedViewModel.offerId = "${responseModelApplicationStore?.offers?.get(0)?.id}"

                        setView(responseModelApplicationStore)
                        Timber.d("storeApplication: $responseModelApplicationStore")
                    }

                    else -> {}
                }
            }
        }

    }


    private fun selectApplication(
        applicationId: String, offerId: String

    ) {
        Timber.d("selectApplication: $applicationId - $offerId")
        lifecycleScope.launch {
            newLoanViewModel.applicationSelect(
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

                        findNavController().navigate(R.id.action_newLoanFragment_to_PEPFragmentSdk)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setView(modelApplicationStore: ModelApplicationStore?) {
        Timber.d("setView $modelApplicationStore")

        resetValues()

        modelApplicationStore?.offers?.map {

            if (it.status != null) {
                mOfferItems?.add(it)

                Timber.d("setView offeritem $mOfferItems")

                maxTotalPayable = updateMaxValue(it.totalPayable, maxTotalPayable)
                maxMonthlyPayment = updateMaxValue(it.monthlyPayment, maxMonthlyPayment)
                maxLikelyArp = updateMaxValue(it.interestRate, maxLikelyArp)

                radarValue?.add(RadarModel("${it.lender?.name}", true))

            } else {

                radarValue?.add(RadarModel("${it.lender?.name}", false))
                Timber.d("setView offeritem- ${radarValue?.size}")
            }
        }

        if (mOfferItems?.size == 0) {
            binding.layoutScroll.visibility = View.GONE
            binding.btnChooseThisLender.visibility = View.GONE
        } else {
            binding.layoutScroll.visibility = View.VISIBLE
            binding.btnChooseThisLender.visibility = View.VISIBLE
        }

        modelApplicationStore?.offers?.let { mOfferItemsAll?.addAll(it) }

        initRadder()

        adapterOffer?.apply {
            updateTotalPayable(maxTotalPayable)
            updateMonthlyPayment(maxMonthlyPayment)
            updateMaxLikelyArp(maxLikelyArp)
            if (modelApplicationStore != null) {
                updateData(modelApplicationStore)
            }
            notifyDataSetChanged()
        }

    }

    private fun resetValues() {
        Timber.d("resetValues")
        radarValue?.clear()
        mOfferItems?.clear()
        mOfferItemsAll?.clear()
        maxTotalPayable = 0.0
        maxMonthlyPayment = 0.0
        maxLikelyArp = 0.0
    }

    private fun updateMaxValue(value: String?, maxValue: Double): Double {
        Timber.d("updateMaxValue")
        return if (value != null && value.toDouble() > maxValue) value.toDouble() else maxValue
    }

    private fun updateHeader(loanAmount: Int, loanTermInMonth: Int) {
        Timber.d("headerUpdate: loanAmount: $loanAmount loanTermInMonth: $loanTermInMonth")
        binding.tvHeading.text =
            getString(
                R.string.sar_for_months,
                formatter.format(loanAmount),
                loanTermInMonth.toString()
            )

    }

    private fun updateSeekBar(loanAmounts: LoanAmounts?) {
        Timber.d("seekBarUpdate: loanAmounts: $loanAmounts ")

        val minLoanAmounts = loanAmounts?.min?.toDouble()?.toInt() ?: 0
        val maxLoanAmounts = loanAmounts?.max?.toDouble()?.toInt() ?: 0

        minLoanAmount=minLoanAmounts

        binding.sbLoanAmount.max = maxLoanAmounts
        binding.sbLoanAmount.progress = _loanAmount

        binding.tvMinLoanAmountValue.text = formatter.format(minLoanAmounts)
        binding.tvMaxLoanAmountValue.text = formatter.format(maxLoanAmounts)

        binding.sbLoanTerms.progress = _loanMonth
    }


    private fun initView() {
        Timber.d("initView")

        if (!isChooseYourLoanDialogShowed) {
            getFields()
        }

        if (mOfferItems != null) {
            adapterOffer = AdapterOffer(
                mOfferItems,
                onItemClicked = { offers, applicationStoreInfo, position, maxTotalPayable, maxMonthlyPayment, maxLikelyArp ->
                    sharedViewModel.applicationStoreInfo = applicationStoreInfo
                    sharedViewModel.offersInfo = offers
                    sharedViewModel.position = position
                    sharedViewModel.maxTotalPayable = maxTotalPayable
                    sharedViewModel.maxMonthlyPayment = maxMonthlyPayment
                    sharedViewModel.maxLikelyArp = maxLikelyArp

                    findNavController().navigate(R.id.action_newLoanFragment_to_loanDetailsFragment)

                },
                onItemSelect = { sharedViewModel.offerId = it.toString() }
            )
        }
        binding.rvOffer.layoutManager = object : LinearLayoutManager(activity) {
            override fun canScrollVertically(): Boolean = false
        }

        binding.rvOffer.adapter = adapterOffer
        PagerSnapHelper().attachToRecyclerView(binding.rvOffer)

        if (isChooseYourLoanDialogShowed) {
            setView(responseModelApplicationStore)
            updateSeekBar(loanAmounts)
        }

    }

    private fun getFields() {
        viewLifecycleOwner.lifecycleScope.launch {
            chooseYourLoanViewModel.getFields.collect {
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
                        val data = it.data?.modelFields
                        if (!isChooseYourLoanDialogShowed) {
                            initChooseYourLoanDialog(data)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    //Convert a URL to Drawable
    private fun loadImageFromUrl(imageUrl: String, callback: (Drawable?) -> Unit) {
        Glide.with(requireContext())
            .load(imageUrl)
//            .override(140,140)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the case where the image loading is cleared or canceled.
                    callback(null)
                }
            })
    }

    private fun handleMirroring() {
        lifecycleScope.launch {
            dataStoreViewModel.getStringValue(AppConstants.DataStorePref.LOCALE).filterNotNull()
                .collectLatest { localeCode ->
                    binding.layoutSdkToolbar.ivPath1648.scaleX = if (localeCode == "ar") -1f else 1f
                    binding.layoutSdkToolbar.root.visibility = View.VISIBLE
                }
        }
    }

}