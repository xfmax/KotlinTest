package com.base.mykotlintest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        countDownController.enterAnimation();
        buttonStart.setOnClickListener {
            countDownController.type = CountDownController.TYPE_TIME;
            countDownController.startTimerSchedule(120.0,120.0)
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
