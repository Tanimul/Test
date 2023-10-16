package bd.com.media365.ratehammer_sdk.extention

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getConnectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.color(color: Int): Int = ContextCompat.getColor(this, color)

fun Context.getDisplayWidth(): Int = resources.displayMetrics.widthPixels

fun Context.getDisplayHeight(): Int = resources.displayMetrics.heightPixels

fun Context.convertDpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Context.getResourceName(resourceId: Int): String? {
    try {
        val resourceEntryName = resources.getResourceEntryName(resourceId)
        val resourceTypeName = resources.getResourceTypeName(resourceId)

//        return "R.$resourceTypeName.$resourceEntryName
        return "$resourceEntryName"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}