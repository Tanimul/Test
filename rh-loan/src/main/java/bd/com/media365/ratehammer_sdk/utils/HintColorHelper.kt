package bd.com.media365.ratehammer_sdk.utils

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan


object HintColorHelper {

    //Using for adding * after adding data binding
    fun createSpannableTextWithRedAsterisk(text: String) = SpannableStringBuilder(text).apply {
        val additionalText = " *"
        val start = length
        append(additionalText)
        val end = length
        setSpan(
            ForegroundColorSpan(Color.RED),
            start,
            end,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}
