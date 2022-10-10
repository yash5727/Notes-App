package com.example.notesapp.Utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.notesapp.R

class PermissionAlertDialogHelper(context: Context) : BaseDialogHelper() {
    //  dialog view
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.permission_alert_dialog, null)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    //  Title textview
    val title: TextView by lazy {
        dialogView.findViewById(R.id.permission_title)
    }

    //  Description textview
    val desc: TextView by lazy {
        dialogView.findViewById(R.id.permission_desc)
    }

    //  Positive button
    val doneButton: TextView by lazy {
        dialogView.findViewById(R.id.positive_button)
    }

    //  Negative button
    val closeButton: TextView by lazy {
        dialogView.findViewById(R.id.negative_button)
    }

    //  closeIconClickListener with listener
    fun closeIconClickListener(func: (() -> Unit)? = null) =
        with(closeButton) {
            setClickListenerToDialogIcon(func)
        }

    //  doneIconClickListener with listener
    fun doneIconClickListener(func: (() -> Unit)? = null) =
        with(doneButton) {
            setClickListenerToDialogIcon(func)
        }

    //  view click listener as extension function
    private fun View.setClickListenerToDialogIcon(func: (() -> Unit)?) =
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }
}