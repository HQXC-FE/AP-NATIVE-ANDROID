package com.xtree.bet.weight

import android.content.Context
import android.util.AttributeSet

/**
 * 无论用户如何尝试移动光标，光标都会被强制到 EditText 末尾
 */
class NoCursorMoveEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        val textLength = text?.length ?: 0
        if (selStart != textLength || selEnd != textLength) {
            setSelection(textLength) // 强制光标始终在末尾
        }
    }
}
