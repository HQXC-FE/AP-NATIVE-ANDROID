package com.xtree.lottery.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.xtree.lottery.R

class PlusMinusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val numberEditText: EditText
    private val addButton: TextView
    private val subtractButton: TextView
    private val tvTitle: TextView
    private val tvPercentage: TextView
    private var maxValue = 200 // 设置最大值
    private val minValue = 1   // 设置最小值

    init {
        // 加载布局文件
        LayoutInflater.from(context).inflate(R.layout.view_plus_minus, this, true)

        numberEditText = findViewById(R.id.et_number)
        addButton = findViewById(R.id.tv_add)
        subtractButton = findViewById(R.id.tv_subtract)
        tvTitle = findViewById(R.id.tv_title)
        tvPercentage = findViewById(R.id.tv_percentage)


        // 初始化默认值为 "1" 并添加 TextWatcher
        numberEditText.setText("1")
        numberEditText.addTextChangedListener(object : TextWatcher {
            private var currentText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == currentText) return  // 避免重复更新
                if (s.isNullOrEmpty()) {
                    return
                }
                // 解析整数
                val number = s.toString().toIntOrNull() ?: minValue

                // 检查是否在范围内
                val finalNumber = when {
                    number > maxValue -> maxValue
                    number < minValue -> minValue
                    else -> number
                }

                // 更新内容
                currentText = finalNumber.toString()
                numberEditText.setText(currentText)
                numberEditText.setSelection(currentText.length)  // 将光标定位在文本末尾
            }
        })
        numberEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // 当 EditText 失去焦点时执行的逻辑
                val text = numberEditText.text.toString()
                if (text.isEmpty()) {
                    numberEditText.setText("1")  // 设置默认值，防止空输入
                }
            }
        }



        addButton.setOnClickListener {
            changeNumber(1)
        }

        subtractButton.setOnClickListener {
            changeNumber(-1)
        }
    }

    private fun changeNumber(value: Int) {
        val text = numberEditText.text.toString()
        val number = text.toIntOrNull() ?: minValue
        val newNumber = (number + value).coerceIn(minValue, maxValue)  // 限制范围
        numberEditText.setText(newNumber.toString())
    }

    // 获取当前数值
    fun getNumber(): Int {
        return numberEditText.text.toString().toIntOrNull() ?: minValue
    }

    // 设置初始数值
    fun setNumber(number: Int, title: String, max: Int, percentage: Boolean = false) {
        maxValue = max
        numberEditText.setText(number.toString())
        tvTitle.text = title
        if (percentage) {
            tvPercentage.visibility = View.VISIBLE
        } else {
            tvPercentage.visibility = View.GONE
        }

    }
}
