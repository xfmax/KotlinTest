package com.base.mykotlintest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * 圆形倒计时进度条
 *
 */
class ProgressView : View {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mRectF: RectF
    private val mPaint: Paint

    /**
     * 画笔颜色
     */
    private var paintColor = 0

    /**
     * 进度条圆弧宽度
     */
    private val mStrokeWidth = 20

    /**
     * 最大进度
     */
    private var mMaxProgress = 100f

    /**
     * 当前进度
     */
    private var mProgress = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var width = this.width
        var height = this.height
        if (width != height) {
            val min = Math.min(width, height)
            width = min
            height = min
        }

        //位置
        mRectF.left = mStrokeWidth / 2.toFloat()
        mRectF.top = mStrokeWidth / 2.toFloat()
        mRectF.right = width - mStrokeWidth / 2.toFloat()
        mRectF.bottom = height - mStrokeWidth / 2.toFloat()

        //设置画布为透明
        canvas.drawColor(Color.TRANSPARENT)

        //画个半透明的圆当背景
        mPaint.color = Color.parseColor("#33000000")
        mPaint.style = Paint.Style.FILL
        canvas.drawOval(mRectF, mPaint)


        //设置画笔
        mPaint.isAntiAlias = true
        mPaint.color = paintColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        canvas.drawArc(mRectF, -90f, mProgress / mMaxProgress * 360, false, mPaint)
    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    fun setPaintColor(color: String?) {
        paintColor = Color.parseColor(color)
    }

    /**
     * 设置到最大进度的时间
     *
     * @param time 倒计时时间 毫秒值
     */
    fun startDownTime(text: TextView, time: Int, listener: OnFinishListener?) {
        mMaxProgress = 100f
        var n = time;
        Thread(Runnable {
            for (i in 0..mMaxProgress.toInt()) {
                try {
                    Thread.sleep((time / mMaxProgress).toLong())
                    //当倒计时结束时通知
                    if (i == 100 && listener != null) {
                        post { listener.onFinish() }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                mProgress = i.toFloat()
                n -= (time / mMaxProgress).toInt();
                text.post { text.text = n.toString();}
                this@ProgressView.postInvalidate()
            }
        }).start()
    }

    init {
        mRectF = RectF()
        mPaint = Paint()
    }
}
