package com.example.ambient_app

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

class FragmentViewModel : ViewModel() {
    // Used to load fragment
    var currentFragment = "HOME"

    // Media Player States
    var rainPlaying = false
    var cricketsPlaying = false
}