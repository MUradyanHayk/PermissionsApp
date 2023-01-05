package com.example.permissionsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissionsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.feature1Button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                onCameraPermissionGranted()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RQ_PERMISSION_FOR_FEATURE_1_CODE)
            }
        }
        binding.feature2Button.setOnClickListener { }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RQ_PERMISSION_FOR_FEATURE_1_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do something with camera here
                    onCameraPermissionGranted()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            // show dialog with explanation here
                            askUserForOpeningAppSettings()
                        } else {
                            // oops, can't do anything
                            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                        }
                    }


                }
            }
            RQ_PERMISSION_FOR_FEATURE_2_CODE -> {}
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
        if (packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(this, "Permission are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Permission denied")
                .setMessage(
                    "You have denied permission forever. " +
                            "You can change your decision in app settings.\n\n" +
                            "Would you like to open app settings?"
                )
                .setPositiveButton("Open") { _, _ -> startActivity(appSettingsIntent) }
                .create()
                .show()
        }

    }

    private fun onCameraPermissionGranted() {
        Toast.makeText(this, "Camera permission is granted", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RQ_PERMISSION_FOR_FEATURE_1_CODE = 1
        const val RQ_PERMISSION_FOR_FEATURE_2_CODE = 2
    }
}