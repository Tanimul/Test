package bd.com.media365.ratehammer_sdk.extention

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import bd.com.media365.ratehammer_sdk.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.DecimalFormat

fun ImageView.loadImageFromDrawable(@DrawableRes aPlaceHolderImage: Int) {
    Glide.with(this.context).load(aPlaceHolderImage).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}

@BindingAdapter("imageUrl")
fun ImageView.loadImageUrl(imageUrl: String?) {
    try {
        if (imageUrl != null) {
            Glide.with(this.context)
                .load(imageUrl)
                .placeholder(R.drawable.logo_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.logo_placeholder)
                .into(this)
        } else {
            loadImageFromDrawable(R.drawable.logo_placeholder)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

@BindingAdapter("formattedNumber")
fun TextView.setFormattedNumber(value: String) {
    val formatter = DecimalFormat("#,###,###")
    val formattedValue = formatter.format(value.toDouble())
    text = formattedValue
}