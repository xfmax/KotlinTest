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
 * @author sufeng
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
    private val mRectFInner: RectF
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
    private var mProgress: Double = 0.0
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 画最外层的背景大圆
        val centre = width / 2 //获取圆心的x坐标

        val radius: Int = centre

        //设置画布为透明
        canvas.drawColor(Color.TRANSPARENT)

        //画个半透明的圆当背景
        mPaint.color = Color.parseColor("#20000000")
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(centre.toFloat(), centre.toFloat(), radius.toFloat(), mPaint) //画出圆环

        //进度条绘制
        mPaint.isAntiAlias = true
        mPaint.color = paintColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth.toFloat() * 2;
        val value = mProgress / mMaxProgress * 360;
        mRectFInner.left = mStrokeWidth.toFloat()
        mRectFInner.top = mStrokeWidth.toFloat()
        mRectFInner.right = width - mStrokeWidth.toFloat()
        mRectFInner.bottom = height - mStrokeWidth.toFloat()
        canvas.drawArc(mRectFInner, -90f, value.toFloat(), false, mPaint)

        //内层背景的白色圆
        mPaint.color = Color.parseColor("#ffffff")
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(
            centre.toFloat(),
            centre.toFloat(),
            (radius - mStrokeWidth).toFloat(),
            mPaint
        )

    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    fun setPaintColor(color: String?) {
        paintColor = Color.parseColor(color)
    }

    fun reset() {
        mProgress = 0.0
        postInvalidate()
    }


    fun startNumberDownTime(text: TextView, time: Double, type: Int) {
        text.post {
            text.text =
                if (type == CountDownController.TYPE_TIME) TimeConvertUtils.convertSecondTo0000String(
                    time.toLong(),
                    true
                ) else time.toInt().toString();
        }
    }


    fun startProgressDownTime(progressValue: Double) {
        mProgress = progressValue * mMaxProgress
        this@ProgressView.invalidate()

    }

    init {
        mRectF = RectF()
        mRectFInner = RectF()
        mPaint = Paint()
    }
}
