package bd.com.media365.rh_android_sdk_test

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RateHammerApp : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()

    }

    companion object {
        private lateinit var instance: RateHammerApp
        fun getInstance(): RateHammerApp {
            return instance
        }
    }
}