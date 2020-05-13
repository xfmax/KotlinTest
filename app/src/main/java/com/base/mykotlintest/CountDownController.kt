package com.base.mykotlintest

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import java.math.BigDecimal

class CountDownController : ConstraintLayout, NewCountdownTimerHelper.OnCountdownTimerCallback {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        /**
         * 计时类型
         */
        const val TYPE_TIME: Int = 0;

        /**
         * 计次类型
         */
        const val TYPE_COUNT: Int = 0;
    }

    private val DELAY_TIME: Long = 0
    private val type: Int? = null;

    private var scheduledTimerNumber: ScheduledTimer? = null
    private var scheduledTimerProgress: ScheduledTimer? = null

    init {
        scheduledTimerNumber = ScheduledTimer()
        scheduledTimerProgress = ScheduledTimer()
    }

    /**
     * 当前的数值（倒计次数 or 倒计时数）
     */
    private var totalNumber: Double = 0.0;
    private var currentNumber: Double = 0.0;

//    private val countTimer: NewCountdownTimerHelper by lazy {
//        NewCountdownTimerHelper(Int.MAX_VALUE, totalNumber, this, true)
//    }

    public fun startTimerSchedule(number: Double) {
        totalNumber = number
        currentNumber = number
        pause()
        progress.setPaintColor("#24C789")
        scheduledTimerNumber!!.scheduleAtFixedRate({
            progress.startNumberDownTime(textNumber, currentNumber--)
        }, DELAY_TIME, DateUtils.SECOND_IN_MILLIS)
        var value = currentNumber
        scheduledTimerProgress!!.scheduleAtFixedRate({
            value = BigDecimal(value).minus(BigDecimal(0.2)).setScale(2,BigDecimal.ROUND_DOWN).toDouble()
            Log.d("ww",","+value.toString())
            progress.startProgressDownTime(value, totalNumber)
        }, DELAY_TIME, 200)
    }

//    public fun start(number: Int) {
//
//        //设置进度条颜色
////        progress.setPaintColor("#ffffff")
////        var n = number
////        progress.startDownTime(textNumber, n, OnFinishListener { Log.d("xbase", "finish") })
//        totalNumber = number
//        progress.setPaintColor("#24C789")
//        countTimer.start(0, 200)
//    }

    public fun reset() {
        progress.setPaintColor("#24C789")
        progress.reset()
        textNumber.text = ""
        currentNumber = totalNumber
        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun pause() {
        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun resume() {
//        progress.setPaintColor("#24C789")
//        scheduledTimerNumber!!.scheduleAtFixedRate({
//            progress.startNumberDownTime(textNumber, currentNumber--)
//        }, DateUtils.SECOND_IN_MILLIS, DateUtils.SECOND_IN_MILLIS)
//
//        scheduledTimerProgress!!.scheduleAtFixedRate({
//            progress.startProgressDownTime( currentNumber--, totalNumber)
//        }, DateUtils.SECOND_IN_MILLIS, DateUtils.SECOND_IN_MILLIS)
    }

    override fun onCountdown(index: Int) {
        Log.d("xbase", index.toString())
//        progress.startDownTime(textNumber, index, totalNumber)
    }

    override fun onComplete() {
        Log.d("xbase", "Complete")
    }

}
