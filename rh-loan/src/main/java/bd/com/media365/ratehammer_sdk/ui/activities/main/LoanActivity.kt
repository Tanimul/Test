package bd.com.media365.ratehammer_sdk.ui.activities.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import bd.com.media365.ratehammer_sdk.base.BaseActivity
import bd.com.media365.ratehammer_sdk.common.AuthenticationService
import bd.com.media365.ratehammer_sdk.common.viewmodels.DataStoreViewModel
import bd.com.media365.ratehammer_sdk.constants.AppConstants
import bd.com.media365.ratehammer_sdk.databinding.ActivitySdkLoanBinding
import bd.com.media365.ratehammer_sdk.extention.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class LoanActivity : BaseActivity<ActivitySdkLoanBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun getViewBinding(): ActivitySdkLoanBinding = ActivitySdkLoanBinding.inflate(layoutInflater)
    override fun init(savedInstanceState: Bundle?) {

        val sessionToken = intent.getStringExtra("session_token").toString()
        val locale = intent.getStringExtra("locale").toString()
        Timber.d("extras: $locale - $sessionToken")
        if (isValidIntent(sessionToken, locale)) {
            lifecycleScope.launch {
                dataStoreViewModel.saveToDataStore(
                    AppConstants.DataStorePref.SESSION_TOKEN,
                    "$sessionToken"
                )
                dataStoreViewModel.saveToDataStore(AppConstants.DataStorePref.LOCALE, "$locale")

            }

            changeAppLanguage(this@LoanActivity, locale)

        }

    }


    //<editor-fold desc="Check Valid  Intent">
    private fun isValidIntent(sessionToken: String, locale: String): Boolean {
        return isValidSessionToken(sessionToken) && isValidLocale(locale)

    }

    private fun isValidSessionToken(sessionToken: String): Boolean {
        val authenticationService = AuthenticationService()
        return authenticationService.isTokenValid(sessionToken)

    }

    private fun isValidLocale(locale: String): Boolean {
        return locale == "ar" || locale == "en"
    }
    //</editor-fold>

    private fun changeAppLanguage(context: Context, languageCode: String?) {
        val resources = context.resources
        val configuration = resources.configuration
        val locale = Locale(languageCode)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        handleLayoutDirection(languageCode)

    }

    private fun handleLayoutDirection(languageCode: String?) {
        if (languageCode == "en") {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
    }
}
