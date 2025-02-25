package com.xtree.live.message;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@IntDef({UserType.TOURIST,UserType.ADMIN, UserType.REGISTER})
public @interface UserType {
    int TOURIST = 0;
    int ADMIN = 1;
    int REGISTER = 2;
}
