package com.xtree.live.player;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
@IntDef({StreamSourceType.SOURCE_RTC, StreamSourceType.SOURCE_BASE, StreamSourceType.SOURCE_M3U8,
        StreamSourceType.SOURCE_RTMP, StreamSourceType.SOURCE_MP4, StreamSourceType.SOURCE_FLV, StreamSourceType.SOURCE_ARTC})
public @interface StreamSourceType {
    int SOURCE_RTC = 0;
    int SOURCE_BASE = 1;
    int SOURCE_M3U8 = 2;
    int SOURCE_RTMP = 3;
    int SOURCE_MP4 = 4;
    int SOURCE_FLV = 5;
    int SOURCE_ARTC = 6;
}
