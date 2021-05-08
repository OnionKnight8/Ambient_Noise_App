package com.example.ambient_app

import android.content.*
import android.graphics.Color
import android.graphics.ColorFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import kotlin.math.ln

class MainActivity : AppCompatActivity(), AddDialog.DialogListener{
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
    private lateinit var settingsFragment: SettingsFragment

    // Shared Preferences
    private val pref = "isDark"
    private lateinit var sharedPref: SharedPreferences

    // Set up view model for fragment and database
    private val model: FragmentViewModel by viewModels()
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory((application as Application).favoritesRepository)
    }

    // Navview
    private lateinit var navView: BottomNavigationView

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

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        // Instantiate Fragments
        homeFragment = HomeFragment()
        favoritesFragment = FavoritesFragment()
        alarmFragment = AlarmFragment()
        settingsFragment = SettingsFragment()

        var selectedFragment: Fragment = homeFragment
        when(model.currentFragment) {
            "HOME" -> selectedFragment = homeFragment
            "FAVORITES" -> selectedFragment = favoritesFragment
            "ALARM" -> selectedFragment = alarmFragment
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

        // Register to receive messages from adapter
        val observer = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.getStringExtra("ACTION") == "PLAY") {
                    val content = intent.getStringArrayListExtra("CONTENT")
                    val volume = intent.getStringArrayListExtra("VOLUME")
                    playFavorite(content, volume)
                }
                else if(intent?.getStringExtra("ACTION") == "DELETE") {
                    val favoritesId = intent.getIntExtra("ID", -1)
                    deleteFavorite(favoritesId)
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(observer,
            IntentFilter("ADAPTER"))
    }

    // Handle Resume (Resume Media)
    override fun onResume() {
        super.onResume()
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

        // Set media player status
        if(model.rainPlaying) {rain.start()}
        if(model.cricketsPlaying) {crickets.start()}
        if(model.beachPlaying) {beach.start()}
        if(model.cityPlaying) {city.start()}
        if(model.restaurantPlaying) {restaurant.start()}
        if(model.noisePlaying) {noise.start()}

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

        stopAll("DESTROY")
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

    // Plays a favorite
    private fun playFavorite(content: ArrayList<String>?, volume: ArrayList<String>?) {
        // Perform off of UI thread
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                stopAll("SAVE")

                // Rain
                if(content!![0].toInt() == 1) {
                    rain.start()
                    model.rainPlaying = true
                    val volumeVal = volume!![0].toInt()
                    rain.setVolume(getVolume(volumeVal), getVolume(volumeVal))
                    model.rainVolume = volumeVal
                } else {
                    rain.setVolume(getVolume(5),getVolume(5))
                    model.rainVolume = 5
                }
                // Crickets
                if(content[1].toInt() == 1) {
                    crickets.start()
                    model.cricketsPlaying = true
                    val volumeVal = volume!![1].toInt()
                    crickets.setVolume(getVolume(volumeVal), getVolume(volumeVal))
                    model.cricketsVolume = volumeVal
                } else {
                    crickets.setVolume(getVolume(5),getVolume(5))
                    model.cricketsVolume = 5
                }

                // Beach
                if(content[2].toInt() == 1) {
                    beach.start()
                    model.beachPlaying = true
                    val volumeVal = volume!![2].toInt()
                    beach.setVolume(getVolume(volumeVal), getVolume(volumeVal))
                    model.beachVolume = volumeVal
                } else {
                    beach.setVolume(getVolume(5),getVolume(5))
                    model.beachVolume = 5
                }
                // City
                if(content[3].toInt() == 1) {
                    city.start()
                    model.cityPlaying = true
                    val volumeVal = volume!![3].toInt()
                    city.setVolume(getVolume(volumeVal), getVolume(volumeVal))
                    model.cityVolume = volumeVal
                } else {
                    city.setVolume(getVolume(5),getVolume(5))
                    model.cityVolume = 5
                }
                // Restaurant
                if(content[4].toInt() == 1) {
                    restaurant.start()
                    model.restaurantPlaying = true
                    val volumeVal = volume!![4].toInt()
                    restaurant.setVolume(getVolume(volumeVal), getVolume(volumeVal))
                    model.restaurantVolume = volumeVal
                } else {
                    restaurant.setVolume(getVolume(5),getVolume(5))
                    model.restaurantVolume = 5
                }
                // White Noise
                if(content[5].toInt() == 1) {
                    noise.start()
                    model.noisePlaying = true
                    val volumeVal = volume!![5].toInt()
                    noise.setVolume(getVolume(volumeVal), getVolume(volumeVal))
                    model.noiseVolume = volumeVal
                } else {
                    noise.setVolume(getVolume(5),getVolume(5))
                    model.noiseVolume = 5
                }
            } catch (e: IllegalStateException) {
                Log.e("Error", e.stackTraceToString())
            }
        }
    }

    // Stops all sounds, called from settings fragment
    fun stopAllCall(view: View) {
        stopAll("SAVE")
    }

    // Stops all sounds. Releases media players on activity recreation.
    private fun stopAll(type: String) {
        if(rain != null) {
            if(type == "DESTROY") {
                rain.stop()
                rain.reset()
                rain.release()
            }
            else if(rain.isPlaying) {
                rain.pause()
                model.rainPlaying = false
            }
        }

        if(crickets != null) {
            if(type == "DESTROY") {
                crickets.stop()
                crickets.reset()
                crickets.release()
            }
            else if(model.cricketsPlaying){
                crickets.pause()
                model.cricketsPlaying = false
            }
        }
        if(beach != null) {
            if(type == "DESTROY") {
                beach.stop()
                beach.reset()
                beach.release()
            }
            else if(beach.isPlaying) {
                beach.pause()
                model.beachPlaying = false
            }
        }
        if(city != null) {
            if(type == "DESTROY") {
                city.stop()
                city.reset()
                city.release()
            }
            else if(city.isPlaying) {
                city.pause()
                model.cityPlaying = false
            }
        }
        if(restaurant != null) {
            if(type == "DESTROY") {
                restaurant.stop()
                restaurant.reset()
                restaurant.release()
            }
            else if(restaurant.isPlaying) {
                restaurant.pause()
                model.restaurantPlaying = false
            }
        }
        if(noise != null) {
            if(type == "DESTROY") {
                noise.stop()
                noise.reset()
                noise.release()
            }
            else if(noise.isPlaying) {
                noise.pause()
                model.noisePlaying = false
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

    // Add to favorites, handled in favorites fragment
    fun addNew(view: View) {
        favoritesFragment.addNew(view)
    }

    // Used to insert into favorites database, overrides interface in AddDialog
    override fun applyText(name: String) {
        // Check if text is empty
        if(name.isNotEmpty()) {
            // Check if text is too long
            if(name.length <= 30) {
                // Set values
                var allEmpty = true

                var rainPlaying = 0
                var rainVolume = 0
                if (model.rainPlaying) {
                    rainPlaying = 1
                    rainVolume = model.rainVolume
                    allEmpty = false
                }
                var cricketsPlaying = 0
                var cricketsVolume = 0
                if (model.cricketsPlaying) {
                    cricketsPlaying = 1
                    cricketsVolume = model.cricketsVolume
                    allEmpty = false
                }
                var beachPlaying = 0
                var beachVolume = 0
                if (model.beachPlaying) {
                    beachPlaying = 1
                    beachVolume = model.beachVolume
                    allEmpty = false
                }
                var cityPlaying = 0
                var cityVolume = 0
                if (model.cityPlaying) {
                    cityPlaying = 1
                    cityVolume = model.cityVolume
                    allEmpty = false
                }
                var restaurantPlaying = 0
                var restaurantVolume = 0
                if (model.restaurantPlaying) {
                    restaurantPlaying = 1
                    restaurantVolume = model.restaurantVolume
                    allEmpty = false
                }
                var noisePlaying = 0
                var noiseVolume = 0
                if (model.noisePlaying) {
                    noisePlaying = 1
                    noiseVolume = model.noiseVolume
                    allEmpty = false
                }
                // Check to make sure at least one sound is playing
                if (!allEmpty) {
                    val contentList = mutableListOf<String>(rainPlaying.toString(), cricketsPlaying.toString(),
                            beachPlaying.toString(), cityPlaying.toString(), restaurantPlaying.toString(),
                            noisePlaying.toString())
                    val volumeList = mutableListOf<String>(rainVolume.toString(), cricketsVolume.toString(),
                            beachVolume.toString(), cityVolume.toString(), restaurantVolume.toString(),
                            noiseVolume.toString())
                    val newFavorite = FavoritesEntry(name = name, content = contentList, volume = volumeList)
                    viewModel.insert(newFavorite)
                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.toast_no_sound), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, getString(R.string.toast_too_long), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@MainActivity, getString(R.string.toast_empty), Toast.LENGTH_SHORT).show()
        }
    }

    // Delete a favorite from list
    fun deleteFavorite(id: Int) {
        if(id != -1) {
            viewModel.deleteId(id)
        }
    }

    // Handle credits button
    fun viewCredits(view: View) {
        settingsFragment.viewCredits(view)
    }

    // Starts timer, disables navbar
    fun startCountdown(view: View) {
        navView.menu.getItem(0).isEnabled = false
        navView.menu.getItem(1).isEnabled = false
        navView.menu.getItem(2).isEnabled = false
        navView.menu.getItem(3).isEnabled = false
        alarmFragment.startCountdown(view)
    }

    // Pauses timer, enables navbar
    fun pauseCountdown(view: View) {
        navView.menu.getItem(0).isEnabled = true
        navView.menu.getItem(1).isEnabled = true
        navView.menu.getItem(2).isEnabled = true
        navView.menu.getItem(2).isEnabled = true
        alarmFragment.pauseCountdown(view)
    }

    // Re-enables navbar and stops sounds.
    fun timerStop() {
        navView.menu.getItem(0).isEnabled = true
        navView.menu.getItem(1).isEnabled = true
        navView.menu.getItem(2).isEnabled = true
        navView.menu.getItem(2).isEnabled = true
        stopAll("SAVE")
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