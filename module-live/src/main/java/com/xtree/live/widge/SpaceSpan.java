package com.xtree.live.widge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpaceSpan extends ReplacementSpan {

    private final int   width;
    private final Paint paint = new Paint();

    public SpaceSpan(final int width) {
        this(width, Color.TRANSPARENT);
    }

    public SpaceSpan(final int width, final int color) {
        super();
        this.width = width;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public int getSize(@NonNull final Paint paint, final CharSequence text,
                       @IntRange(from = 0) final int start,
                       @IntRange(from = 0) final int end,
                       @Nullable final Paint.FontMetricsInt fm) {
        return width;
    }

    @Override
    public void draw(@NonNull final Canvas canvas, final CharSequence text,
                     @IntRange(from = 0) final int start,
                     @IntRange(from = 0) final int end,
                     final float x, final int top, final int y, final int bottom,
                     @NonNull final Paint paint) {
        canvas.drawRect(x, top, x + width, bottom, this.paint);
    }
}
