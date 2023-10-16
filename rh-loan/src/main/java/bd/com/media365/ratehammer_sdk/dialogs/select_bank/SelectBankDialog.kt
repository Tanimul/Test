package bd.com.media365.ratehammer_sdk.dialogs.select_bank

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import bd.com.media365.ratehammer_sdk.adapter.AdapterBank
import bd.com.media365.ratehammer_sdk.databinding.DialogSdkSelectBankBinding
import bd.com.media365.ratehammer_sdk.models.banks.response.Data
import java.text.DecimalFormat


class SelectBankDialog(
    lifecycleOwner: LifecycleOwner,
    private val selectBankViewModel: SelectBankViewModel,
    private val bankList: ArrayList<Data>?,
    private val context: Context,
    private val acceptClick: (Data) -> Unit,
    private val cancelClick: () -> Unit
) {
    private var dialog: Dialog? = null
    val binding = DialogSdkSelectBankBinding.inflate(LayoutInflater.from(context))
    private lateinit var adapterBank: AdapterBank

    init {

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(binding.root)

        binding.viewModel = selectBankViewModel
        binding.lifecycleOwner = lifecycleOwner

        initBanks()

        // Set the dialog cancelable
        dialogBuilder.setCancelable(true)


        dialog = dialogBuilder.create()
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)


        // Set an onCancelListener to handle outside clicks
        dialog?.setOnCancelListener { dialogInterface ->
            // Handle the outside click here
            // For example, you can dismiss the dialog
            cancelClick.invoke()
            dialogInterface.dismiss()
        }


        //TextWatcher to the search EditText
        binding.etSelectBank.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the bankList based on the user's search input
                val searchText = s.toString().trim()
                val filteredList = bankList?.filter { data ->
                    data.name!!.contains(searchText, true)
                }
                if (filteredList != null) {
                    adapterBank.updateData(filteredList)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }


    private fun initBanks() {
        if (bankList != null) {
            adapterBank = AdapterBank(
                bankList
            ) { data ->
                acceptClick.invoke(data)
                dismiss()
            }
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        with(binding.rvBankList) {

            setHasFixedSize(true)
            this.layoutManager = layoutManager
            adapter = adapterBank

        }

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