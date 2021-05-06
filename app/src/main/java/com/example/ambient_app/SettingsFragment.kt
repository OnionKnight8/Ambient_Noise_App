package com.example.ambient_app

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {
    // Assets
    private val light = R.drawable.ic_baseline_light_mode_24
    private val dark = R.drawable.ic_baseline_dark_mode_24

    // Views
    private lateinit var buttonTheme: ImageButton
    private lateinit var textTheme: TextView

    // Shared Preferences
    private val pref = "isDark"
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        sharedPref = this.requireActivity().getSharedPreferences(pref, Context.MODE_PRIVATE)
        buttonTheme = view.findViewById<ImageButton>(R.id.buttonTheme)
        textTheme = view.findViewById<TextView>(R.id.textTheme)
        changeButton()
        return view
    }

    // Handler for image button, handled is MainActivity
    fun swapTheme() {}

    // Swap button and text view display based on current mode
    private fun changeButton() {
        val isDark = sharedPref.getBoolean(pref, true)
        if(isDark) {
            buttonTheme.setImageResource(dark)
            textTheme.text = getString(R.string.text_dark_mode)
        } else {
            buttonTheme.setImageResource(light)
            textTheme.text = getString(R.string.text_light_mode)
        }
    }
}