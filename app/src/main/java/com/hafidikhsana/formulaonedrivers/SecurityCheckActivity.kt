package com.hafidikhsana.formulaonedrivers

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.scottyab.rootbeer.RootBeer


class SecurityCheckActivity : AppCompatActivity() {

    lateinit var isActiveAccessibilityService: TextView
    lateinit var isEmulator: TextView
    lateinit var isRoot: TextView
    lateinit var isUSBDebuging: TextView
    lateinit var rootBeer:RootBeer

    private fun isAccessibilityEnabled(ctx: Context): Boolean {
        val am = ctx.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.isEnabled
    }

    private fun isEmulator(): Boolean {
        // https://stackoverflow.com/questions/2799097/how-can-i-detect-when-an-android-application-is-running-in-the-emulator
        return ((Build.MANUFACTURER == "Google" && Build.BRAND == "google" &&
                ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                        && Build.FINGERPRINT.endsWith(":user/release-keys")
                        && Build.PRODUCT.startsWith("sdk_gphone_")
                        && Build.MODEL.startsWith("sdk_gphone_"))
                        || (Build.FINGERPRINT.startsWith("google/sdk_gphone64_")
                        && (Build.FINGERPRINT.endsWith(":userdebug/dev-keys")
                        || Build.FINGERPRINT.endsWith(":user/release-keys"))
                        && Build.PRODUCT.startsWith("sdk_gphone64_")
                        && Build.MODEL.startsWith("sdk_gphone64_")))))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_check)

        // Is active accessibility service
        isActiveAccessibilityService = findViewById(R.id.activeAccessibilityServiceText)

        if (isAccessibilityEnabled(this)) {
            isActiveAccessibilityService.text = "Device is active accessibility service"
        } else {
            isActiveAccessibilityService.text = "Device is clear"
        }

        // Is emulator
        isEmulator = findViewById(R.id.emulatorText)

        if (isEmulator()) {
            isEmulator.text = "Running on Emulator"
        } else {
            isEmulator.text = "Running on Physical Device"
        }

        // Is root
        rootBeer = RootBeer(this);
        isRoot = findViewById(R.id.rootText)

        if (rootBeer.isRooted) {
            isRoot.text = "Device is Root"
        } else {
            isRoot.text = "Device is Clear"
        }

        // Is USB debuging
        isUSBDebuging = findViewById(R.id.USBDebugingText)

        // https://stackoverflow.com/questions/18716808/how-to-check-usb-debugging-enabled-programmatically
        if(Settings.Secure.getInt(this.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1) {
            isUSBDebuging.text = "Using USB Debuging"
        } else {
            isUSBDebuging.text = "Using Emulator"
        }
    }
}