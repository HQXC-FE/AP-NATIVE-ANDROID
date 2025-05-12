package com.xtree.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.xtree.base.R;
import com.xtree.base.utils.ConvertUtils;

public class MaxHeightNetsedScrollView extends NestedScrollView {
    private int mMaxHeight;

    public MaxHeightNetsedScrollView(@NonNull Context context) {
        super(context);
    }

    public MaxHeightNetsedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MaxHeightNetsedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public void setmMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightNetsedScrollView);
        //mMaxHeight = typedArray.getLayoutDimension(R.styleable.MaxHeightNetsedScrollView_maxHeight, mMaxHeight);
        mMaxHeight = ConvertUtils.px2dp((float) (ConvertUtils.getScreenHeight(context) * 0.6));
        mMaxHeight = ConvertUtils.dp2px(mMaxHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight > 0 && mMaxHeight < height) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
