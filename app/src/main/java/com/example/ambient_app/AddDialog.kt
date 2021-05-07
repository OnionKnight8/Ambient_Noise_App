package com.example.ambient_app

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import java.lang.ClassCastException

class AddDialog : AppCompatDialogFragment() {
    private lateinit var editName: EditText
    private lateinit var listener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_new, null)

        // Setup Listener
        try {
            listener = context as DialogListener
        } catch(e: ClassCastException) {
            e.printStackTrace()
        }

        builder.setView(view).setTitle(getString(R.string.button_add))
                .setNegativeButton("Cancel") { dialog, which ->

                }.setPositiveButton("Accept") { dialog, which ->
                    val name = editName.text.toString()
                    listener.applyText(name)
                }

        if (view != null) {
            editName = view.findViewById(R.id.editName)
        }

        return builder.create()
    }

    // Used to add information from dialog
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch(e: ClassCastException) {
            e.printStackTrace()
        }
    }

    // Listener for dialog
    interface DialogListener {
        fun applyText(name: String)
    }
}