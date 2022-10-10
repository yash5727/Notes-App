package com.example.notesapp.Utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

inline fun Fragment.showPermissionAlertDialog(func: PermissionAlertDialogHelper.() -> Unit): AlertDialog =
    PermissionAlertDialogHelper(this.requireContext()).apply {
        func()
    }.create()


fun Context.isBiometricsSupported(): Boolean {
    val biometricManager = BiometricManager.from(this)
    var isBiometricsSupported = false

    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            isBiometricsSupported = true
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Log.println(Log.INFO, "a", "Logger: BIOMETRIC_ERROR_NO_HARDWARE")
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Log.println(Log.INFO, "a", "Logger: BIOMETRIC_ERROR_NONE_ENROLLED")
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Log.println(Log.ERROR, "a", "Logger: BIOMETRIC_ERROR_HW_UNAVAILABLE")
        }
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            TODO()
        }
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            TODO()
        }
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
            TODO()
        }
    }
    return isBiometricsSupported
}