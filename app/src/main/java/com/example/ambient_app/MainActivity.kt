package com.example.ambient_app

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(){
    // Media Players
    private lateinit var rain: MediaPlayer
    private lateinit var crickets: MediaPlayer

    // Fragments
    private lateinit var homeFragment: HomeFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var alarmFragment: AlarmFragment
    private lateinit var notesFragment: NotesFragment
    private lateinit var settingsFragment: SettingsFragment

    // Shared Preferences
    private val pref = "isDark"
    lateinit var sharedPref: SharedPreferences

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

        // Restore media players upon app restart
        if(savedInstanceState != null) {
            // Get Media Player Positions
            rain.seekTo(savedInstanceState.getInt("rainPos"))
            crickets.seekTo(savedInstanceState.getInt("cricketsPos"))

            // Set media player status and resume playback
            model.rainPlaying = savedInstanceState.getBoolean("rainPlaying")
            if(model.rainPlaying) {rain.start()}
            model.cricketsPlaying = savedInstanceState.getBoolean("cricketsPlaying")
            if(model.cricketsPlaying) {crickets.start()}
        }
    }

    // Save variables on restart
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentFragment", model.currentFragment)
        outState.putBoolean("rainPlaying", model.rainPlaying)
        outState.putBoolean("cricketsPlaying", model.cricketsPlaying)
        if(model.rainPlaying) {
            outState.putInt("rainPos", rain.currentPosition)
        }
        if(model.cricketsPlaying) {
            outState.putInt("cricketsPos", crickets.currentPosition)
        }

        // Release old Media Players
        rain.stop()
        crickets.stop()
        rain.release()
        crickets.release()
    }

    /*
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore Fragment
        val currentFragment = savedInstanceState.getString("currentFragment")
        if(currentFragment != null) {
            model.currentFragment = currentFragment
        } else { model.currentFragment = "HOME" }

        // Get Media Player Positions
        rain.seekTo(savedInstanceState.getInt("rainPos"))
        crickets.seekTo(savedInstanceState.getInt("cricketsPos"))

        // Set media player status and resume playback
        model.rainPlaying = savedInstanceState.getBoolean("rainPlaying")
        if(model.rainPlaying) {rain.start()}
        model.cricketsPlaying = savedInstanceState.getBoolean("cricketsPlaying")
        if(model.cricketsPlaying) {crickets.start()}
    }
     */

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
        }
        homeFragment.controlPlayback(view)
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
}