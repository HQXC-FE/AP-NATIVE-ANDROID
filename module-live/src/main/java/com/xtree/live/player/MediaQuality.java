package com.xtree.live.player;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(SOURCE)
@IntDef({MediaQuality.SD, MediaQuality.HD, MediaQuality.UHD, MediaQuality._4K})
public @interface MediaQuality {
    int SD = 0;
    int HD = 1;

    int UHD = 2;

    int _4K= 3;
}
