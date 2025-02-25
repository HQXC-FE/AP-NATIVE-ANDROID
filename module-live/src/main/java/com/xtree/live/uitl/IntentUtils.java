package com.xtree.live.uitl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IntentUtils {

    public static void jumpToBrowser(@NonNull Context context, @Nullable String url) {
        if(TextUtils.isEmpty(url))return;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        try {
            Uri contentUrl = Uri.parse(url);
            intent.setData(contentUrl);
            context.startActivity(intent);
        }catch (Exception e) {}
    }

}
