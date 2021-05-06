package com.example.ambient_app

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
        }
    }
}