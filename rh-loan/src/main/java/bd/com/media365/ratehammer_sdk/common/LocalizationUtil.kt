package bd.com.media365.ratehammer_sdk.common

import android.content.Context
import androidx.annotation.StringRes
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import bd.com.media365.ratehammer_sdk.utils.DataStorePrefUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalizationUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStorePrefUtils: DataStorePrefUtils,
//    private val roomDatabase: MyRoomDatabase
) {
    //    private val isResourcesFetchFlow =
//        dataStorePrefUtils.getBooleanValue(AppConstants.DataStorePref.RESOURCES_FETCH)
    private val localeFlow = dataStorePrefUtils.getStringValue(AppConstants.DataStorePref.LOCALE)

    //    private suspend fun isResourcesFetch(): Boolean = isResourcesFetchFlow.first()
    private fun isResourcesFetch(): Boolean = true
    private suspend fun getLocale(): String = localeFlow.first().toString()

    suspend fun getLocalizedText(@StringRes resId: Int): String {
        val isResourcesFetch = isResourcesFetch()
        val locale = getLocale()

        return context.getString(resId)

//        return if (isResourcesFetch) {
//            context.getString(resId)
//        } else {
//            getLocalizedTextFromDatabase(resId, context.getResourceName(resId), locale)
//        }
    }

//    private suspend fun getLocalizedTextFromDatabase(
//        @StringRes resId: Int, resourcesKey: String?, locale: String
//    ): String {
//        Timber.d("getLocalesString: $resourcesKey")
//        val modelLocales = getLocalizedTextByKeyFromDatabase("$resourcesKey")
//
//        val localizedText = if (locale == "en") {
//            modelLocales.en
//        } else {
//            modelLocales.ar
//        }
//
//        return localizedText ?: context.getString(resId)
//    }

//    private suspend fun getLocalizedTextByKeyFromDatabase(key: String): ModelLocales {
//        return roomDatabase.localesDao().fetchSingleLocales(key).firstOrNull() ?: ModelLocales()
//    }

}