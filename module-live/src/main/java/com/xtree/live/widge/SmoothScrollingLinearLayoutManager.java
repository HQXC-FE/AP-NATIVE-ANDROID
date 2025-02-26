package com.xtree.live.widge;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class SmoothScrollingLinearLayoutManager extends LinearLayoutManager {

    //范围0.5f到2f
    private final static float MAX_TIME_PER_PIXEL = 0.8f;
    private final static float MIN_TIME_PER_PIXEL = 0.5f;
    public SmoothScrollingLinearLayoutManager(Context context, boolean reverseLayout) {
        super(context, RecyclerView.VERTICAL, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, int position, float size) {
        final LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                float speed = MAX_TIME_PER_PIXEL - 0.003f * size;

                speed = Math.max(Math.min(MAX_TIME_PER_PIXEL, speed), MIN_TIME_PER_PIXEL);

                return speed;
            }
        };

        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }
}
