package com.xtree.live.widge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.blankj.utilcode.util.ConvertUtils;

import me.xtree.mvvmhabit.base.BaseApplication;

public class TagSpan extends ReplacementSpan {
    public @ColorInt int bgColor,borderColor, textColor;

    public @DrawableRes int iconRes;

    public int paddingL, paddingT, paddingR, paddingB;

    private final int IMAGE_SIZE = ConvertUtils.dp2px (16);
    private final int DIVIDER_IMAGE_TEXT = ConvertUtils.dp2px (2);


    private Paint mBgPaint;
    public TagSpan() {
        mBgPaint = new Paint();
    }

    private int mSpanWidth;
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSpanWidth = IMAGE_SIZE +  ((int) paint.measureText(text, start, end) + paddingL + paddingR) + DIVIDER_IMAGE_TEXT;
        return mSpanWidth;
    }


    private final Rect sourceRect = new Rect();
    private final Rect dstRect = new Rect();

    private final RectF bgRect = new RectF();
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int originalColor = paint.getColor();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        float baseline = y;
        if(bgColor != 0){
            mBgPaint.setStyle(Paint.Style.FILL);
            mBgPaint.setColor(this.bgColor);
            //画圆角矩形背景
            bgRect.left = (int) x;
            bgRect.top = top;
            bgRect.right = bgRect.left + mSpanWidth;
            bgRect.bottom = bottom;
            float radio = bgRect.width() / 2f;
            canvas.drawRoundRect(bgRect,
                    radio,
                    radio,
                    mBgPaint);

            if(this.borderColor != 0){
                mBgPaint.setStyle(Paint.Style.STROKE);
                mBgPaint.setColor(this.borderColor);
                mBgPaint.setStrokeWidth(2);
                canvas.drawRoundRect(bgRect,
                        radio,
                        radio,
                        mBgPaint);
            }
            float distance=(fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            baseline = (bottom -  top) / 2f + distance;
        }

        int textStart = paddingL;
        if(iconRes != 0){
            Bitmap bitmap = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), iconRes);
            float rate = bitmap.getWidth() * 1f / bitmap.getHeight();
            sourceRect.left = 0;
            sourceRect.top = 0;
            sourceRect.right = bitmap.getWidth();
            sourceRect.bottom = bitmap.getHeight();

            int iconStart = (int) x + paddingL;
            int iconEnd = iconStart + IMAGE_SIZE;

            dstRect.top = top + DIVIDER_IMAGE_TEXT;
            dstRect.bottom = bottom - DIVIDER_IMAGE_TEXT;

            int dstWidth = (int) (dstRect.height() * rate);
            int dalta = (iconEnd - iconStart - dstWidth) / 2;
            dstRect.left = iconStart + dalta;
            dstRect.right = iconEnd - dalta;
            canvas.drawBitmap(bitmap,sourceRect, dstRect, paint);
            textStart =  iconEnd + DIVIDER_IMAGE_TEXT;
        }

        if(textColor == 0){
            paint.setColor(originalColor);
        }else {
            paint.setColor(textColor);
        }
        canvas.drawText(text, start, end, textStart, baseline, paint);
        //将paint复原
        paint.setColor(originalColor);
    }
}
