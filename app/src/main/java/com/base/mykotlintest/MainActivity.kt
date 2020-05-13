package com.base.mykotlintest

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener {
            countDownController.startTimerSchedule(10.0)
        }

        buttonEnd.setOnClickListener {
            if (buttonEnd.text == "END") {
                countDownController.pause()
                buttonEnd.text = "RESUME"
            }else{
                countDownController.resume()
                buttonEnd.text = "END"
            }
        }


        buttonReset.setOnClickListener {
            countDownController.reset()
        }
    }

}
