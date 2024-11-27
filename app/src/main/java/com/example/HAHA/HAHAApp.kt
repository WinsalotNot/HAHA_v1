package com.example.HAHA

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.firebase.FirebaseApp
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.api.model.CustomColorTheme

class HAHAApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Midtrans SDK
        UiKitApi.Builder()
            .withMerchantClientKey(BuildConfig.CLIENT_KEY) // Your Midtrans client key
            .withContext(this) // Application context
            .withMerchantUrl(BuildConfig.BASE_URL) // Your backend URL
            .enableLog(true) // Optional: Enable logs for debugging
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // Optional: Custom color theme
            .build()

        // Set language for the SDK
        setLocaleNew("en") // 'en' for English, 'id' for Bahasa
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
