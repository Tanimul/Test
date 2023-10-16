package bd.com.media365.ratehammer_sdk.dialogs.choose_loan

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.LifecycleOwner
import bd.com.media365.ratehammer_sdk.R
import bd.com.media365.ratehammer_sdk.databinding.DialogSdkChooseYourLoanBinding
import bd.com.media365.ratehammer_sdk.models.fields.response.LoanAmounts
import bd.com.media365.ratehammer_sdk.models.fields.response.LoanPurposes
import bd.com.media365.ratehammer_sdk.models.fields.response.LoanTerms
import java.text.DecimalFormat


class ChooseYourLoanDialog(
    lifecycleOwner: LifecycleOwner,
    private val chooseYourLoanViewModel: ChooseYourLoanViewModel,
    private val loanTerms: List<LoanTerms>,
    private val loanPurpose: List<LoanPurposes>,
    private val loanAmounts: LoanAmounts?,
    private val context: Context,
    private val acceptClick: (String, Int, String) -> Unit,
    private val cancelClick: () -> Unit
) {
    private var dialog: Dialog? = null
    val binding = DialogSdkChooseYourLoanBinding.inflate(LayoutInflater.from(context))
    private lateinit var selectedLoanPurposes: LoanPurposes
    private lateinit var selectedLoanTerms: LoanTerms
    val formatter = DecimalFormat("#,###,###")
    var loanAmount: String = ""

    init {

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(binding.root)

        binding.viewModel = chooseYourLoanViewModel
        binding.modelFields=chooseYourLoanViewModel.getFields.value.data?.modelFields
        binding.lifecycleOwner = lifecycleOwner

        initFields()

        binding.tvChooseLoanTerm.setOnClickListener {
            binding.tvChooseLoanTerm.showDropDown()
        }
        binding.tvChooseLoanPurpose.setOnClickListener {
            binding.tvChooseLoanPurpose.showDropDown()
        }
        binding.ibCancel.setOnClickListener {
            cancelClick.invoke()
            dismiss()
        }

        // Set the dialog cancelable
        dialogBuilder.setCancelable(true)


        binding.btnImReady.setOnClickListener {
            val loanTerms = selectedLoanTerms.value ?: ""
            val loanPurposes = selectedLoanPurposes.value ?: 0
            acceptClick.invoke(
                loanTerms,
                loanPurposes,
                loanAmount
            )
            dismiss()
        }

        val stepSize = 500
        binding.sbLoanAmount.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val minValue = (loanAmounts?.min?.toDouble() ?: 0).toInt()

                // Ensure that progress is updated in steps of 100
                val adjustedProgress =
                    if (progress < minValue) minValue else (progress / stepSize) * stepSize

                binding.tvLoanAmountValue.text = formatter.format(adjustedProgress)
                loanAmount = adjustedProgress.toString()
                if (minValue == adjustedProgress) {
                    seekBar.progress = 0
                } else {
                    seekBar.progress = adjustedProgress
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })


        binding.tvChooseLoanTerm.setOnItemClickListener { _, _, position, _ ->
            updateButtonState()

            selectedLoanTerms = loanTerms[position]

        }

        binding.tvChooseLoanPurpose.setOnItemClickListener { _, _, position, _ ->
            updateButtonState()

            selectedLoanPurposes = loanPurpose[position]

        }

        dialog = dialogBuilder.create()
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)


        // Set an onCancelListener to handle outside clicks
        dialog?.setOnCancelListener { dialogInterface ->
            // Handle the outside click here
            // For example, you can dismiss the dialog
            cancelClick.invoke()
            dialogInterface.dismiss()
        }

    }

    private fun updateButtonState() {
        val isLoanTermSelected = binding.tvChooseLoanTerm.text.isNotBlank()
        val isLoanPurposeSelected = binding.tvChooseLoanPurpose.text.isNotBlank()
        val allFieldsFilled = isLoanTermSelected && isLoanPurposeSelected

        binding.btnImReady.isEnabled = allFieldsFilled

        if (allFieldsFilled) {
            binding.btnImReady.setBackgroundResource(R.drawable.ripple_theme_gradient_curve)
        } else {
            binding.btnImReady.setBackgroundResource(R.drawable.background_button)
        }
    }

    private fun initFields() {

        loanAmount = loanAmounts?.min.toString()

        val chooseLoanTermAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.custom_sdk_spinner_item,
            loanTerms.map { it.name }
        )

        val chooseLoanPurposeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context,
            R.layout.custom_sdk_spinner_item,
            loanPurpose.map { it.name }
        )

        binding.tvChooseLoanTerm.setAdapter(chooseLoanTermAdapter)
        binding.tvChooseLoanPurpose.setAdapter(chooseLoanPurposeAdapter)
    }

    fun show() {

        dialog!!.show()
    }

    private val isShowing: Boolean
        get() = dialog!!.isShowing

    private fun dismiss() {
        if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
    }

    fun clearDialog() {
        if (isShowing)
            dismiss()
        dialog = null
    }

    fun dismiss(activity: Activity) {
        if (activity.isDestroyed) return
        if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
    }

}