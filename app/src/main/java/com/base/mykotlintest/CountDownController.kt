package com.base.mykotlintest

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*

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

    private val type: Int? = null;

    /**
     * 当前的数值（倒计次数 or 倒计时数）
     */
    private var totalNumber: Int = 0;

    private val countTimer: NewCountdownTimerHelper by lazy {
        NewCountdownTimerHelper(Int.MAX_VALUE, totalNumber, this, true)
    }

    public fun start(number: Int) {

        //设置进度条颜色
//        progress.setPaintColor("#ffffff")
//        var n = number
//        progress.startDownTime(textNumber, n, OnFinishListener { Log.d("xbase", "finish") })
        totalNumber = number

        countTimer.start(0, 1000)
    }

    public fun reset() {
        countTimer.reset()
    }

    public fun pause() {
        countTimer.pause()
    }

    public fun resume() {
        countTimer.resume()
    }

    override fun onCountdown(index: Int) {
        Log.d("xbase", index.toString())
        progress.startDownTime(textNumber,index,totalNumber,null)
    }

    override fun onComplete() {
        Log.d("xbase", "Complete")
    }

}
