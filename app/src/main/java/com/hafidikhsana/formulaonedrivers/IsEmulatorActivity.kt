package com.hafidikhsana.formulaonedrivers

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class IsEmulatorActivity : AppCompatActivity() {

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "sdk" || Build.PRODUCT == "google_sdk"
                || Build.PRODUCT == "sdk_google_phone" || Build.PRODUCT == "vbox86p")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_is_emulator)

        val isEmulatorTextView: TextView = findViewById(R.id.isEmulatorTextView)

        if (isEmulator()) {
            isEmulatorTextView.text = "Running on Emulator"
        } else {
            isEmulatorTextView.text = "Running on Physical Device"
        }
    }
}