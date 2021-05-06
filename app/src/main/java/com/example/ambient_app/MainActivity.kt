package com.example.ambient_app

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import kotlin.math.ln

class MainActivity : AppCompatActivity(){
    // Media Players
    private lateinit var rain: MediaPlayer
    private lateinit var crickets: MediaPlayer
    private lateinit var beach: MediaPlayer
    private lateinit var city: MediaPlayer
    private lateinit var restaurant: MediaPlayer
    private lateinit var noise: MediaPlayer

    // Fragments
    private lateinit var homeFragment: HomeFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var alarmFragment: AlarmFragment
    private lateinit var notesFragment: NotesFragment
    private lateinit var settingsFragment: SettingsFragment

    // Shared Preferences
    private val pref = "isDark"
    private lateinit var sharedPref: SharedPreferences

    // Set up view model
    private val model: FragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore Fragment
        if(savedInstanceState != null) {
            val currentFragment = savedInstanceState.getString("currentFragment")
            if(currentFragment != null) {
                model.currentFragment = currentFragment
            } else { model.currentFragment = "HOME" }
        }

        // Setup or retrieve theme from preferences
        sharedPref = getSharedPreferences(pref, Context.MODE_PRIVATE)
        val isDark = sharedPref.getBoolean(pref, true)
        if(isDark) {
            // Dark Mode is Default
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Instantiate Fragments
        homeFragment = HomeFragment()
        favoritesFragment = FavoritesFragment()
        alarmFragment = AlarmFragment()
        notesFragment = NotesFragment()
        settingsFragment = SettingsFragment()

        var selectedFragment: Fragment = homeFragment
        when(model.currentFragment) {
            "HOME" -> selectedFragment = homeFragment
            "FAVORITES" -> selectedFragment = favoritesFragment
            "ALARM" -> selectedFragment = alarmFragment
            "NOTES" -> selectedFragment = notesFragment
            "SETTINGS" -> selectedFragment = settingsFragment
        }

        // Start with whatever fragment is saved in view model, "home" by default
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_container,
            selectedFragment).addToBackStack( "tag" ).commit()

        // Handles navigation bar items
        navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {selectedFragment = homeFragment
                model.currentFragment = "HOME"}
                R.id.navigation_favorites -> {selectedFragment = favoritesFragment
                model.currentFragment = "FAVORITES"}
                R.id.navigation_alarm -> {selectedFragment = alarmFragment
                model.currentFragment = "ALARM"}
                R.id.navigation_notes -> {selectedFragment = notesFragment
                model.currentFragment = "NOTES"}
                R.id.navigation_settings -> {selectedFragment = settingsFragment
                model.currentFragment = "SETTINGS"}
            }
            //model.currentFragment = selectedFragment
            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_container,
                    selectedFragment).addToBackStack("tag").commit()

            true
        }

        // Instantiate Media Players
        rain = MediaPlayer.create(this, R.raw.thunderstorm)
        rain.isLooping = true
        crickets = MediaPlayer.create(this.baseContext, R.raw.crickets)
        crickets.isLooping = true
        beach = MediaPlayer.create(this.baseContext, R.raw.beach)
        beach.isLooping = true
        city = MediaPlayer.create(this.baseContext, R.raw.cityscape)
        city.isLooping = true
        restaurant = MediaPlayer.create(this.baseContext, R.raw.restaurant)
        restaurant.isLooping = true
        noise = MediaPlayer.create(this.baseContext, R.raw.white_noise)
        noise.isLooping = true

        // Restore media players upon app restart
        if(savedInstanceState != null) {
            // Get Media Player Positions
            rain.seekTo(savedInstanceState.getInt("rainPos"))
            crickets.seekTo(savedInstanceState.getInt("cricketsPos"))
            beach.seekTo(savedInstanceState.getInt("beachPos"))
            city.seekTo(savedInstanceState.getInt("cityPos"))
            restaurant.seekTo(savedInstanceState.getInt("restaurantPos"))
            noise.seekTo(savedInstanceState.getInt("noisePos"))

            // Set Volumes in View Model
            model.rainVolume = savedInstanceState.getInt("rainVolume")
            model.cricketsVolume = savedInstanceState.getInt("cricketsVolume")
            model.beachVolume = savedInstanceState.getInt("beachVolume")
            model.cityVolume = savedInstanceState.getInt("cityVolume")
            model.restaurantVolume = savedInstanceState.getInt("restaurantVolume")
            model.noiseVolume = savedInstanceState.getInt("noiseVolume")

            // Set media player status, set volume, and resume playback
            model.rainPlaying = savedInstanceState.getBoolean("rainPlaying")
            if(model.rainPlaying) {rain.start()}
            model.cricketsPlaying = savedInstanceState.getBoolean("cricketsPlaying")
            if(model.cricketsPlaying) {crickets.start()}
            model.beachPlaying = savedInstanceState.getBoolean("beachPlaying")
            if(model.beachPlaying) {beach.start()}
            model.cityPlaying = savedInstanceState.getBoolean("cityPlaying")
            if(model.cityPlaying) {city.start()}
            model.restaurantPlaying = savedInstanceState.getBoolean("restaurantPlaying")
            if(model.restaurantPlaying) {restaurant.start()}
            model.noisePlaying = savedInstanceState.getBoolean("noisePlaying")
            if(model.noisePlaying) {noise.start()}
        }

        // Set volumes of media players
        rain.setVolume(getVolume(model.rainVolume),getVolume(model.rainVolume))
        crickets.setVolume(getVolume(model.cricketsVolume),getVolume(model.cricketsVolume))
        beach.setVolume(getVolume(model.beachVolume),getVolume(model.beachVolume))
        city.setVolume(getVolume(model.cityVolume),getVolume(model.cityVolume))
        restaurant.setVolume(getVolume(model.restaurantVolume),getVolume(model.restaurantVolume))
        noise.setVolume(getVolume(model.noiseVolume),getVolume(model.noiseVolume))
    }

    // Save variables on restart
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current fragment
        outState.putString("currentFragment", model.currentFragment)

        // Save booleans
        outState.putBoolean("rainPlaying", model.rainPlaying)
        outState.putBoolean("cricketsPlaying", model.cricketsPlaying)
        outState.putBoolean("beachPlaying", model.beachPlaying)
        outState.putBoolean("cityPlaying", model.cityPlaying)
        outState.putBoolean("restaurantPlaying", model.restaurantPlaying)
        outState.putBoolean("noisePlaying", model.noisePlaying)

        // Save volumes
        outState.putInt("rainVolume", model.rainVolume)
        outState.putInt("cricketsVolume", model.cricketsVolume)
        outState.putInt("beachVolume", model.beachVolume)
        outState.putInt("cityVolume", model.cityVolume)
        outState.putInt("restaurantVolume", model.restaurantVolume)
        outState.putInt("noiseVolume", model.noiseVolume)

        // Save positions
        if(model.rainPlaying) {
            outState.putInt("rainPos", rain.currentPosition)
        }
        if(model.cricketsPlaying) {
            outState.putInt("cricketsPos", crickets.currentPosition)
        }
        if(model.beachPlaying) {
            outState.putInt("beachPos", beach.currentPosition)
        }
        if(model.cityPlaying) {
            outState.putInt("cityPos", city.currentPosition)
        }
        if(model.restaurantPlaying) {
            outState.putInt("restaurantPos", restaurant.currentPosition)
        }
        if(model.noisePlaying) {
            outState.putInt("noisePos", noise.currentPosition)
        }

        // Release old Media Players
        rain.stop()
        crickets.stop()
        beach.stop()
        city.stop()
        restaurant.stop()
        noise.stop()
        rain.release()
        crickets.release()
        beach.release()
        city.release()
        restaurant.release()
        noise.release()
    }

    // Controls media playback with listeners in home fragment
    fun controlPlayback(view: View) {
        when(view.id) {
            R.id.buttonPlayRain -> {
                if (rain.isPlaying) {
                    rain.pause()
                    model.rainPlaying = false
                } else {
                    rain.start()
                    model.rainPlaying = true
                }
            }
            R.id.buttonPlayCrickets -> {
                if (crickets.isPlaying) {
                    crickets.pause()
                    model.cricketsPlaying = false
                } else {
                    crickets.start()
                    model.cricketsPlaying = true
                }
            }
            R.id.buttonPlayBeach -> {
                if (beach.isPlaying) {
                    beach.pause()
                    model.beachPlaying = false
                } else {
                    beach.start()
                    model.beachPlaying = true
                }
            }
            R.id.buttonPlayCity -> {
                if (city.isPlaying) {
                    city.pause()
                    model.cityPlaying = false
                } else {
                    city.start()
                    model.cityPlaying = true
                }
            }
            R.id.buttonPlayRestaurant -> {
                if (restaurant.isPlaying) {
                    restaurant.pause()
                    model.restaurantPlaying = false
                } else {
                    restaurant.start()
                    model.restaurantPlaying = true
                }
            }
            R.id.buttonPlayNoise -> {
                if (noise.isPlaying) {
                    noise.pause()
                    model.noisePlaying = false
                } else {
                    noise.start()
                    model.noisePlaying = true
                }
            }
        }
        controlVolume(view)
        homeFragment.controlPlayback(view)
    }

    // Change volume based on seek bars in Home fragment
    fun controlVolume(view: View) {
        when(view.id) {
            R.id.seekBarRain -> {
                val newVolume = getVolume(model.rainVolume)
                rain.setVolume(newVolume, newVolume)
            }
            R.id.seekBarCrickets -> {
                val newVolume = getVolume(model.cricketsVolume)
                crickets.setVolume(newVolume, newVolume)
            }
            R.id.seekBarBeach -> {
                val newVolume = getVolume(model.beachVolume)
                beach.setVolume(newVolume, newVolume)
            }
            R.id.seekBarCity -> {
                val newVolume = getVolume(model.cityVolume)
                city.setVolume(newVolume, newVolume)
            }
            R.id.seekBarRestaurant -> {
                val newVolume = getVolume(model.restaurantVolume)
                restaurant.setVolume(newVolume, newVolume)
            }
            R.id.seekBarNoise -> {
                val newVolume = getVolume(model.noiseVolume)
                noise.setVolume(newVolume, newVolume)
            }
        }
    }

    // Sets up onclick listener for theme swap in settings fragment
    fun swapTheme(view: View) {
        val isDark = sharedPref.getBoolean(pref, true)
        val edit = sharedPref.edit()

        if(isDark) {
            edit.putBoolean(pref, false).apply()
        } else {
            edit.putBoolean(pref, true).apply()
        }
        this.recreate()
    }

    // Converts seek bar values in volumes
    // NOTE: Due to the way that the MediaPlayer.setVolume() method is handled, the value of the
    // volume is set between 0 and 1. This means the best way to handle this is to scale
    // logarithmically. I found the below formula from user "R.A" on Stack Overflow at this link:
    // https://stackoverflow.com/a/45558575/15827298
    private fun getVolume(newVolume: Int): Float {
        return (1 - ln(model.MAX_VOLUME.toFloat() - newVolume.toFloat()) / ln(model.MAX_VOLUME.toFloat()))
    }
}