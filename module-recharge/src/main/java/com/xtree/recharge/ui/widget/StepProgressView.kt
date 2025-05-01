package com.xtree.recharge.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.xtree.recharge.R

class StepProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 属性默认值
    private var stepCount = 4
    private var currentStep = 1
    private var defaultColor = Color.parseColor("#C6C6C6")
    private var activeDrawable: Drawable? = null
    private var activeColor = Color.parseColor("#A17DF5")
    private var circleRadius = 20f
    private var lineWidth = 5f
    private var textSize = 40f
    private var horizontalPadding = 40f
    private var stepTexts: List<String> = listOf() // 步骤文本列表

    init {
        // 加载自定义属性
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.StepProgressView, 0, 0)

            stepCount = typedArray.getInteger(R.styleable.StepProgressView_stepCount, stepCount)
            currentStep =
                typedArray.getInteger(R.styleable.StepProgressView_currentStep, currentStep)
            defaultColor =
                typedArray.getColor(R.styleable.StepProgressView_defaultColor, defaultColor)
            activeColor = typedArray.getColor(R.styleable.StepProgressView_activeColor, activeColor)
            activeDrawable = typedArray.getDrawable(R.styleable.StepProgressView_activeDrawable)
            circleRadius =
                typedArray.getDimension(R.styleable.StepProgressView_circleRadius, circleRadius)
            lineWidth = typedArray.getDimension(R.styleable.StepProgressView_lineWidth, lineWidth)
            textSize = typedArray.getDimension(R.styleable.StepProgressView_textSize, textSize)
            horizontalPadding = typedArray.getDimension(
                R.styleable.StepProgressView_horizontalPadding,
                horizontalPadding
            )

            // 解析步骤文本数组
            val stepTextsResId = typedArray.getResourceId(R.styleable.StepProgressView_stepTexts, 0)
            if (stepTextsResId != 0) {
                stepTexts = resources.getStringArray(stepTextsResId).toList()
            } else {
                // 提供默认值
                stepTexts = (1..stepCount).map { "步骤 $it" }
            }

            typedArray.recycle()
        }

        // 初始化画笔
        textPaint.color = defaultColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (circleRadius * 2 + textSize + 40).toInt() // 40是文字与圆的间距
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val widthPerStep = (width - horizontalPadding * 2 - circleRadius * 2) / (stepCount - 1)

        for (i in 0 until stepCount) {
            val cx = horizontalPadding + circleRadius + i * widthPerStep
            val cy = height - circleRadius

            // Draw lines
            if (i < stepCount - 1) {
                paint.color = if (i < currentStep - 1) activeColor else defaultColor
                paint.strokeWidth = lineWidth
                canvas.drawLine(cx, cy, cx + widthPerStep, cy, paint)
            }

            // 绘制圆圈
            if (i < currentStep) {
                activeDrawable?.let { drawable ->
                    // 设置 Drawable 的范围
                    drawable.setBounds(
                        (cx - circleRadius).toInt(),
                        (cy - circleRadius).toInt(),
                        (cx + circleRadius).toInt(),
                        (cy + circleRadius).toInt()
                    )
                    drawable.draw(canvas) // 绘制 Drawable
                } ?: run {
                    // 如果未设置 Drawable，则使用默认颜色绘制圆圈
                    paint.color = activeColor
                    canvas.drawCircle(cx, cy, circleRadius, paint)
                }
            } else {
                // 绘制未激活的圆圈
                paint.color = defaultColor
                canvas.drawCircle(cx, cy, circleRadius, paint)
            }

            // Draw text
            val text = if (i < stepTexts.size) stepTexts[i] else "步骤 ${i + 1}"
            textPaint.color = if (i < currentStep) activeColor else defaultColor
            canvas.drawText(text, cx, cy - circleRadius - 40, textPaint) // 文字偏移量为40
        }
    }

    // 支持动态更新
    fun setCurrentStep(step: Int) {
        currentStep = step.coerceIn(1, stepCount)
        invalidate()
    }

    fun setStepTexts(texts: List<String>) {
        stepTexts = texts
        invalidate()
    }
}
