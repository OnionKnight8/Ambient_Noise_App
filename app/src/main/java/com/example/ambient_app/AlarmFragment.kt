package com.example.ambient_app

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class AlarmFragment : Fragment() {

    // View Model
    private val model: FragmentViewModel by activityViewModels()

    // Set up number pickers
    private lateinit var hours: NumberPicker
    private lateinit var minutes: NumberPicker
    private lateinit var seconds: NumberPicker
    lateinit var timerThread: Thread
    var timerThreadRunning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)

        // Set up number pickers
        hours = view.findViewById(R.id.numberHour)
        minutes = view.findViewById(R.id.numberMinute)
        seconds = view.findViewById(R.id.numberSecond)

        // Set min and max values
        hours.minValue = 0
        hours.maxValue = 60
        minutes.minValue = 0
        minutes.maxValue = 59
        seconds.minValue = 0
        seconds.maxValue = 59

        // Disable wrapping selector wheel
        hours.wrapSelectorWheel = false
        minutes.wrapSelectorWheel = false
        seconds.wrapSelectorWheel = false

        // Disable keyboard on number pickers
        hours.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS;
        minutes.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS;
        seconds.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS;

        return view
    }

    // When start button is pressed
    fun startCountdown(view: View) {
        var timeCounter: Long = 0
        timeCounter = ((hours.value * 3600) + (minutes.value * 60) + (seconds.value) * 1000L)
        hours.isEnabled = false
        minutes.isEnabled = false
        seconds.isEnabled = false
        timerThreadRunning = true

        // Initiate new thread
        Toast.makeText(context, getString(R.string.toast_timer_start), Toast.LENGTH_LONG).show()
        val countDown = object: Runnable {
            override fun run() {
                while((System.currentTimeMillis() < (System.currentTimeMillis() + timeCounter)) and timerThreadRunning) {
                    synchronized(this){
                        try{
                            Thread.sleep(1000)
                            timerHandler.sendEmptyMessage(0)
                        }catch(e: Exception){}
                    }
                }
            }
        }

        timerThread = Thread(countDown)
        timerThread.start()
    }

    // Handler for countdown thread
    val timerHandler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if(seconds.value >= 1) {
                seconds.value -= 1
            }
            else if(minutes.value >= 1) {
                seconds.value = 59
                minutes.value -= 1
            }
            else if(hours.value >= 1) {
                minutes.value = 59
                seconds.value = 59
                hours.value -= 1
            }
            else {
                timerThreadRunning = false
                hours.isEnabled = true
                minutes.isEnabled = true
                seconds.isEnabled = true
                Toast.makeText(context, getString(R.string.toast_timer_up), Toast.LENGTH_LONG).show()
                val act = activity as MainActivity
                act.timerStop()
            }
        }
    }

    // When pause button is pressed
    fun pauseCountdown(view: View) {
        timerThreadRunning = false
        hours.isEnabled = true
        minutes.isEnabled = true
        seconds.isEnabled = true
    }
}