package com.example.ambient_app

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import java.lang.ClassCastException

class CreditsDialog : AppCompatDialogFragment() {
    private lateinit var editName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_credits, null)

        builder.setView(view).setTitle(getString(R.string.button_credits))
                .setPositiveButton("Accept") { dialog, which ->
                }

        return builder.create()
    }
}