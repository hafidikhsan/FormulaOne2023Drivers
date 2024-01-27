package com.hafidikhsana.formulaonedrivers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.hafidikhsana.formulaonedrivers.databinding.ActivityKotlinRealtimeMlkitBinding

class KotlinRealtimeMLKitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKotlinRealtimeMlkitBinding
    private lateinit var cameraHelper: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKotlinRealtimeMlkitBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        cameraHelper = CameraHelper(
                owner = this,
                context = this.applicationContext,
                viewFinder = binding.cameraView,
                onResult = ::onResult
        )

        cameraHelper.start()
    }

    private fun onResult(result: String) {
        Log.d(TAG, "Result is $result")
        binding.textResult.text = result
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val TAG = "CameraXDemo"
    }
}