package com.example.ambient_app

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment() {

    // Assets
    private val play = R.drawable.ic_baseline_play_circle_filled_24
    private val pause = R.drawable.ic_baseline_pause_circle_filled_24

    // View Model
    private val model: FragmentViewModel by activityViewModels()

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
        seekBar.progress = model.rainVolume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.rainVolume = progress
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRain))}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRain))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRain))}
        })
        seekBar = view.findViewById(R.id.seekBarCrickets)
        seekBar.progress = model.cricketsVolume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.cricketsVolume = progress
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCrickets))}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCrickets))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCrickets))}
        })
        seekBar = view.findViewById(R.id.seekBarBeach)
        seekBar.progress = model.beachVolume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.beachVolume = progress
                thisActivity.controlVolume(view.findViewById(R.id.seekBarBeach))}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarBeach))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarBeach))}
        })
        seekBar = view.findViewById(R.id.seekBarCity)
        seekBar.progress = model.cityVolume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.cityVolume = progress
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCity))}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCity))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarCity))}
        })
        seekBar = view.findViewById(R.id.seekBarRestaurant)
        seekBar.progress = model.restaurantVolume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.restaurantVolume = progress
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRestaurant))}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRestaurant))}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thisActivity.controlVolume(view.findViewById(R.id.seekBarRestaurant))}
        })
        seekBar = view.findViewById(R.id.seekBarNoise)
        seekBar.progress = model.noiseVolume
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.noiseVolume = progress
                thisActivity.controlVolume(view.findViewById(R.id.seekBarNoise))}
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