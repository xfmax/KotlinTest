package com.base.mykotlintest

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import java.math.BigDecimal

class CountDownController : ConstraintLayout{

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
        const val TYPE_COUNT: Int = 1;
    }

    private val DELAY_TIME: Long = 0
    private val PROGRESS_PERIOD: Long = 200
    private var numberPeriod: Long = DateUtils.SECOND_IN_MILLIS
    public var type: Int = 0

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
    private var progressValue: Double = 0.0;
    private var valueUnit: Double = 0.0;

//    private val countTimer: NewCountdownTimerHelper by lazy {
//        NewCountdownTimerHelper(Int.MAX_VALUE, totalNumber, this, true)
//    }

    public fun startTimerSchedule(number: Double,totalTime:Double) {
        totalNumber = number
        currentNumber = number
        valueUnit = totalTime / number;
        numberPeriod = valueUnit.toLong() * 1000;
        Log.d("xbase","MUMBER:"+numberPeriod+",valueUnit:"+valueUnit)
        pause()
        progress.setPaintColor("#24C789")
        scheduledTimerNumber!!.scheduleAtFixedRate({
            if (currentNumber <= 0) reset() else progress.startNumberDownTime(textNumber, currentNumber--,type)
        }, DELAY_TIME, numberPeriod)

        progressValue = totalTime;
        scheduledTimerProgress!!.scheduleAtFixedRate({
            progressValue = BigDecimal(progressValue).minus(BigDecimal(PROGRESS_PERIOD).divide(
                BigDecimal(DateUtils.SECOND_IN_MILLIS)
            )).setScale(2,BigDecimal.ROUND_DOWN).toDouble()
            val progressPercentage = progressValue / totalTime;
            Log.d("ww",progressValue.toString()+",totaltime:"+totalTime)
            if (progressPercentage <= 0) reset() else progress.startProgressDownTime(progressPercentage)
        }, DELAY_TIME, PROGRESS_PERIOD)
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
        progressValue = 0.0
        currentNumber = totalNumber
        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun pause() {
        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun resume() {
        progress.setPaintColor("#24C789")
        scheduledTimerNumber!!.scheduleAtFixedRate({
            progress.startNumberDownTime(textNumber, currentNumber--,type)
        }, DELAY_TIME, numberPeriod)

        scheduledTimerProgress!!.scheduleAtFixedRate({
            progressValue = BigDecimal(progressValue).minus(BigDecimal(0.2)).setScale(2,BigDecimal.ROUND_DOWN).toDouble()
            val progressPercentage = progressValue / totalNumber;
            if (progressPercentage <= 0) reset() else progress.startProgressDownTime(progressPercentage)
        }, DELAY_TIME, PROGRESS_PERIOD)
    }

}
