package com.xtree.live.uitl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class TargetImageView  extends AppCompatImageView {
    private String imagePath;
    private String backgroundPath;

    private final boolean loggable = false;
    public TargetImageView(@NonNull Context context) {
        super(context);
    }

    public TargetImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TargetImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        if(loggable){
            Log.d("TargetImageView", Integer.toHexString(System.identityHashCode(this)) + "    "+ "requestLayout  imagePath：" + imagePath);
        }
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if(loggable){
            Log.d("TargetImageView", Integer.toHexString(System.identityHashCode(this)) + "    " + "setImageDrawable  imagePath：" + imagePath);
        }
        super.setImageDrawable(drawable);

    }
}

