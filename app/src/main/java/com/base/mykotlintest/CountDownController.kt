package com.base.mykotlintest

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.view.*

class CountDownController : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //计时器
    private val mhandle: Handler = Handler()
    private var isPause = false //是否暂停

    private var currentSecond: Long = 0 //当前毫秒数

    private val timeRunable: Runnable = object : Runnable {
        override fun run() {
            currentSecond = currentSecond + 1000
            textNumber.text = currentSecond.toString()
            if (!isPause) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 1000)
            }
        }
    }




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
    private val currentNumber: Int = 0;

    public fun start(number: Int) {

        //设置进度条颜色
//        progress.setPaintColor("#ffffff")
//        var n = number
//        progress.startDownTime(textNumber, n, OnFinishListener { Log.d("xbase", "finish") })

        mhandle.postDelayed(timeRunable,1000)
    }

    public fun reset(number: Int) {

    }

    public fun pause() {
        isPause = true
    }

    public fun resume() {
        isPause = false
    }

    public fun update(number: Int) {
    }

}
