package com.xtree.live.uitl;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class HtmlImageGetter implements Html.ImageGetter {
    private final TextView mText;

    public HtmlImageGetter(TextView text) {
        mText = text;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable drawable = new LevelListDrawable();
        Glide.with(mText.getContext())
                .asBitmap()
                .load(source)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                        drawable.addLevel(1, 1, bitmapDrawable);
                        drawable.setBounds(0, 0, 50, 50);
                        drawable.setLevel(1);

//                            mText.invalidate();
                        mText.setText(mText.getText());
                        mText.refreshDrawableState();
                    }
                });
        return drawable;
    }
}
