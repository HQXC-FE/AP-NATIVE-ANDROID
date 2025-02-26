package com.xtree.live.uitl;

import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class GlideRoundTransform extends BitmapTransformation {

    public GlideRoundTransform(int dp) {
        super();
        roundingRadius = (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }

    private static final String ID = "com.xtree.live.GlideRoundTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final int roundingRadius;

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        if (roundingRadius == 0) {
            return toTransform;
        }

        return TransformationUtils.roundedCorners(pool, toTransform, roundingRadius);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GlideRoundTransform) {
            GlideRoundTransform other = (GlideRoundTransform) o;
            return roundingRadius == other.roundingRadius;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(roundingRadius));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        byte[] radiusData = ByteBuffer.allocate(4).putInt(roundingRadius).array();
        messageDigest.update(radiusData);
    }
}
