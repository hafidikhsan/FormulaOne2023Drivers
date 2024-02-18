package com.hafidikhsana.formulaonedrivers

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.scottyab.rootbeer.RootBeer
import java.lang.RuntimeException


class SecurityCheckActivity : AppCompatActivity() {

    lateinit var isActiveAccessibilityService: TextView
    lateinit var isEmulator: TextView
    lateinit var isRoot: TextView
    lateinit var isUSBDebuging: TextView
    lateinit var rootBeer:RootBeer
    val crashlytics = Firebase.crashlytics
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        throw Exception("Rooted device detected")
    }

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
        val accessibilityEnabled = isAccessibilityEnabled(this)

        if (accessibilityEnabled) {
            isActiveAccessibilityService.text = "Device is active accessibility service"
        } else {
            isActiveAccessibilityService.text = "Device is clear"
        }

        // Is USB debuging
        isUSBDebuging = findViewById(R.id.USBDebugingText)
        val usbDebug = Settings.Secure.getInt(this.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1

        // https://stackoverflow.com/questions/18716808/how-to-check-usb-debugging-enabled-programmatically
        if(usbDebug) {
            isUSBDebuging.text = "Using USB Debuging"
        } else {
            isUSBDebuging.text = "Using Emulator"
        }

        // Is emulator
        isEmulator = findViewById(R.id.emulatorText)
        val emulator = isEmulator()

        if (emulator) {
            isEmulator.text = "Running on Emulator"
        } else {
            isEmulator.text = "Running on Physical Device"
        }

        // Is root
        rootBeer = RootBeer(this);
        isRoot = findViewById(R.id.rootText)
        val rooted = rootBeer.isRooted

        if (rooted) {
            isRoot.text = "Device is Root"
            showDefaultDialog(this)
        } else {
            isRoot.text = "Device is Clear"
        }
    }

    private fun showDefaultDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle("Hey")
            setMessage("You're running in a rooted device!")
            setPositiveButton("Ok", DialogInterface.OnClickListener(function = positiveButtonClick))
        }.create().show()
    }
}