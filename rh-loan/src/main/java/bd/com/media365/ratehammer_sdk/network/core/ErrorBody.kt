package bd.com.media365.ratehammer_sdk.network.core

import androidx.annotation.Keep

@Keep
data class ErrorBody(
    var message: String?,
    var statusCode: Int?,
    var errors: String?
)
