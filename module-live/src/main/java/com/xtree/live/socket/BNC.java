package com.xtree.live.socket;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.live.uitl.MessageUtils;

public interface BNC {
    String SUFFIX = ".bnc";

    @NonNull
    String imagePath();

    @NonNull
    String objectKey();

    static Object loadImageObject(String imagePath, String objectKey) {
        if (TextUtils.isEmpty(imagePath)) return null;
        if (imagePath.endsWith(SUFFIX)) {
            return new BNCObject(imagePath, objectKey);
        }
        return MessageUtils.completeImagePath(imagePath);
    }
}
