package com.example.ambient_app

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

class FragmentViewModel : ViewModel() {
    // Used to load fragment
    var currentFragment = "HOME"

    // Media Player States
    var rainPlaying = false
    var cricketsPlaying = false
    var beachPlaying = false
    var cityPlaying = false
    var restaurantPlaying = false
    var noisePlaying = false

    // Media Player Volume Levels
    var MAX_VOLUME = 10
    var rainVolume = 5
    var cricketsVolume = 5
    var beachVolume = 5
    var cityVolume = 5
    var restaurantVolume = 5
    var noiseVolume = 5
}