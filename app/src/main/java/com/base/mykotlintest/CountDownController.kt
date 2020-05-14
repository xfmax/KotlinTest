package com.base.mykotlintest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import java.math.BigDecimal


class CountDownController : ConstraintLayout {

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
    private var PROGRESS_PERIOD: Long = 200
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

    public fun startTimerSchedule(
        number: Double,
        totalTime: Double,
        currentProgressPercentage: Double
    ) {
        totalNumber = number
        currentNumber = number
        valueUnit = BigDecimal(totalTime).multiply(BigDecimal(currentProgressPercentage)).divide(
            BigDecimal(number), 3,
            BigDecimal.ROUND_HALF_EVEN
        ).toDouble();
        numberPeriod = (valueUnit * 1000).toLong();
        Log.d("xbase", "MUMBER:" + numberPeriod + ",valueUnit:" + valueUnit)
        pause()
        progress.setPaintColor("#24C789")
        scheduledTimerNumber!!.scheduleAtFixedRate({
            if (currentNumber <= 0) reset() else progress.startNumberDownTime(
                textNumber,
                currentNumber--,
                type
            )
        }, DELAY_TIME, numberPeriod)

        progressValue = currentNumber
        val timeUnit = BigDecimal(valueUnit).multiply(BigDecimal(0.2))
        PROGRESS_PERIOD = (timeUnit.multiply(BigDecimal(1000.0))).toLong()
        scheduledTimerProgress!!.scheduleAtFixedRate({
            progressValue =
                BigDecimal(progressValue).minus(timeUnit).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toDouble()
            val progressPercentage: Double = BigDecimal(progressValue).divide(
                BigDecimal(number)/*.multiply(BigDecimal(currentProgressPercentage))*/,
                3,
                BigDecimal.ROUND_HALF_EVEN
            ).toDouble()
            Log.d(
                "qq",
                progressValue.toString() + ",totaltime:" + totalTime + "," + progressPercentage + ",timeUnit:" + timeUnit
            )
            if (progressPercentage <= 0) resetProgress() else progress.startProgressDownTime(
                (1 - progressPercentage)
                /** currentProgressPercentage + currentProgressPercentage*/
            )
        }, DELAY_TIME, PROGRESS_PERIOD)
    }

    public fun updateStepProgress(
        number: Double,
        currentTime: Double,
        position: Double,
        totalDuration: Double
    ) {
        val progressPercentage = BigDecimal(currentTime).minus(BigDecimal(position))
            .setScale(2, BigDecimal.ROUND_HALF_UP).divide(
                BigDecimal(totalDuration), 3, BigDecimal.ROUND_HALF_EVEN
            )
        val currentNumber = BigDecimal(number).multiply(BigDecimal(1.0).minus(progressPercentage))
        Log.d("xbase", "progressPercentage:$progressPercentage,$currentNumber")
        progress.updateProgressAndNumber(
            textNumber,
            currentNumber.toDouble(),
            progressPercentage.toDouble(),
            type
        )
    }

    public fun reset() {
        progress.setPaintColor("#24C789")
        progress.reset()
        textNumber.text = ""
        progressValue = 0.0
        currentNumber = totalNumber
        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun resetProgress() {
        progress.setPaintColor("#24C789")
        progress.reset()
//        textNumber.text = ""
        progressValue = 0.0
//        currentNumber = totalNumber
//        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun pause() {
        scheduledTimerNumber!!.cancel()
        scheduledTimerProgress!!.cancel()
    }

    public fun resume() {
        progress.setPaintColor("#24C789")
        scheduledTimerNumber!!.scheduleAtFixedRate({
            progress.startNumberDownTime(textNumber, currentNumber--, type)
        }, DELAY_TIME, numberPeriod)

        scheduledTimerProgress!!.scheduleAtFixedRate({
            progressValue =
                BigDecimal(progressValue).minus(BigDecimal(0.2)).setScale(2, BigDecimal.ROUND_DOWN)
                    .toDouble()
            val progressPercentage = progressValue / totalNumber;
            if (progressPercentage <= 0) reset() else progress.startProgressDownTime(1 - progressPercentage)
        }, DELAY_TIME, PROGRESS_PERIOD)
    }

    public fun enterAnimation() {
        val scaleX = ObjectAnimator.ofFloat(progress, "scaleX", 0f, 1f, 0.9f)
        val scaleY = ObjectAnimator.ofFloat(progress, "scaleY", 0f, 1f, 0.9f)

        val animatorSet = AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(AccelerateInterpolator());
        animatorSet.playTogether(scaleX, scaleY);//两个动画同时开始
        animatorSet.start();
    }

}
