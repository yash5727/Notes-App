package com.example.notesapp.Utils

import android.content.Context
import android.graphics.Paint
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Utils {

    fun showDialog(
        context: Context,
        overrideThemeResId: Int = com.google.android.material.R.style.Theme_MaterialComponents_Dialog,
        title: String,
        description: String,
        isCancelable: Boolean,
        titleOfPositiveButton: String,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(
            context,
            overrideThemeResId
        )
            .setTitle(title)
            .setMessage(description)
            .setCancelable(isCancelable)
            .setPositiveButton(titleOfPositiveButton) { dialog, _ ->
                positiveButtonFunction?.invoke()
            }

        titleOfNegativeButton?.let {
            builder.setNegativeButton(titleOfNegativeButton) { dialog, _ ->
                negativeButtonFunction?.invoke()
            }
        }

        val alertDialog = builder.create()

        alertDialog.setOnShowListener {
            val neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
            neutralButton.paintFlags = neutralButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        return alertDialog
    }
}