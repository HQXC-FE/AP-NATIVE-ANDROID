package com.xtree.live.message;

import static com.xtree.live.message.DeliverStatus.STATUS_COMPLETE;
import static com.xtree.live.message.DeliverStatus.STATUS_FAILED;
import static com.xtree.live.message.DeliverStatus.STATUS_NONE;
import static com.xtree.live.message.DeliverStatus.STATUS_PENDING;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@IntDef({STATUS_NONE, STATUS_COMPLETE, STATUS_PENDING, STATUS_FAILED, DeliverStatus.STATUS_RETRYING})
public @interface DeliverStatus {
    int STATUS_NONE = -1;
    int STATUS_COMPLETE = 0;
    int STATUS_PENDING = 0x20;
    int STATUS_FAILED = 0x40;
    int STATUS_RETRYING = 0x80;
}