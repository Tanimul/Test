package bd.com.media365.ratehammer_sdk.extention

import bd.com.media365.ratehammer_sdk.common.LocalizationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
fun String.checkIsEmpty(): Boolean =
    isNullOrEmpty() || "" == this || this.equals("null", ignoreCase = true)

fun LocalizationUtil.getLocalizedTextFlow(resourceId: Int, scope: CoroutineScope): StateFlow<String?> {
    return flow {
        emit(getLocalizedText(resourceId))
    }.stateIn(scope, SharingStarted.Lazily, null)
}