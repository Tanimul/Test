package bd.com.media365.ratehammer_sdk.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import bd.com.media365.ratehammer_sdk.R

class LoadingDialog(context: Context?, cancelable: Boolean = false, text: String?) {
    private var dialog: Dialog? = null

    fun show() {
        dialog!!.show()
    }

    private val isShowing: Boolean
        get() = dialog!!.isShowing

    fun dismiss() {
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

    init {
        dialog = Dialog(context!!, android.R.style.Theme_Material_Dialog)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_sdk_loading)
        dialog!!.setCancelable(cancelable)
    }
}