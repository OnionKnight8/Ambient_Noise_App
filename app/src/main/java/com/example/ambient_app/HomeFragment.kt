package com.example.ambient_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class HomeFragment : Fragment() {

    // Assets
    private val play = R.drawable.ic_baseline_play_circle_filled_24
    private val pause = R.drawable.ic_baseline_pause_circle_filled_24

    // View Model
    private val model: FragmentViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate View
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set Button Icons
        var imageButton: ImageButton = view.findViewById(R.id.buttonPlayRain)
        if(model.rainPlaying) {
            imageButton.setImageResource(pause)
        } else {
            imageButton.setImageResource(play)
        }
       imageButton = view.findViewById(R.id.buttonPlayCrickets)
        if(model.cricketsPlaying) {
            imageButton.setImageResource(pause)
        } else {
            imageButton.setImageResource(play)
        }
        imageButton = view.findViewById(R.id.buttonPlayBeach)
        if(model.beachPlaying) {
            imageButton.setImageResource(pause)
        } else {
            imageButton.setImageResource(play)
        }
        imageButton = view.findViewById(R.id.buttonPlayCity)
        if(model.cityPlaying) {
            imageButton.setImageResource(pause)
        } else {
            imageButton.setImageResource(play)
        }
        imageButton = view.findViewById(R.id.buttonPlayRestaurant)
        if(model.restaurantPlaying) {
            imageButton.setImageResource(pause)
        } else {
            imageButton.setImageResource(play)
        }
        imageButton = view.findViewById(R.id.buttonPlayNoise)
        if(model.noisePlaying) {
            imageButton.setImageResource(pause)
        } else {
            imageButton.setImageResource(play)
        }

        // Set Volume Controllers
        var seekBar: SeekBar = view.findViewById(R.id.seekBarRain)
        val thisActivity = activity as MainActivity
        seekBar.setProgress(model.rainVolume, true)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRain))
                model.rainVolume = progress}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRain))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRain))}
        })
        seekBar = view.findViewById(R.id.seekBarCrickets)
        seekBar.setProgress(model.cricketsVolume, true)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCrickets))
                model.cricketsVolume = progress}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCrickets))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCrickets))}
        })
        seekBar = view.findViewById(R.id.seekBarBeach)
        seekBar.setProgress(model.beachVolume, true)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarBeach))
                model.beachVolume = progress}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarBeach))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarBeach))}
        })
        seekBar = view.findViewById(R.id.seekBarCity)
        seekBar.setProgress(model.cityVolume, true)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCity))
                model.cityVolume = progress}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCity))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCity))}
        })
        seekBar = view.findViewById(R.id.seekBarRestaurant)
        seekBar.setProgress(model.restaurantVolume, true)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRestaurant))
                model.restaurantVolume = progress}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRestaurant))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRestaurant))}
        })
        seekBar = view.findViewById(R.id.seekBarNoise)
        seekBar.setProgress(model.noiseVolume, true)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarNoise))
                model.noiseVolume = progress}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarNoise))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarNoise))}
        })

        return view
    }

    // Controls playback of media players when play/pause button is selected.
    fun controlPlayback(view: View) {
        val imageButton = view as ImageButton
        when(imageButton.id) {
            R.id.buttonPlayRain -> {
                if (model.rainPlaying) {
                    imageButton.setImageResource(pause)
                } else {
                    imageButton.setImageResource(play)
                }
            }
            R.id.buttonPlayCrickets -> {
                if (model.cricketsPlaying) {
                    imageButton.setImageResource(pause)
                } else {
                    imageButton.setImageResource(play)
                }
            }
            R.id.buttonPlayBeach -> {
                if (model.beachPlaying) {
                    imageButton.setImageResource(pause)
                } else {
                    imageButton.setImageResource(play)
                }
            }
            R.id.buttonPlayCity -> {
                if (model.cityPlaying) {
                    imageButton.setImageResource(pause)
                } else {
                    imageButton.setImageResource(play)
                }
            }
            R.id.buttonPlayRestaurant -> {
                if (model.restaurantPlaying) {
                    imageButton.setImageResource(pause)
                } else {
                    imageButton.setImageResource(play)
                }
            }
            R.id.buttonPlayNoise -> {
                if (model.noisePlaying) {
                    imageButton.setImageResource(pause)
                } else {
                    imageButton.setImageResource(play)
                }
            }
        }
    }
}