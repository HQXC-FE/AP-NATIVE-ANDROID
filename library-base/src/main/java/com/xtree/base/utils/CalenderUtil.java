package com.xtree.base.utils;

import android.icu.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

public class CalenderUtil {

    public static String searchToday() {
        Calendar calendar = Calendar.getInstance();

        Date newDate = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(newDate);

        return formattedDate;
    }

    public static String cutDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);

        Date newDate = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(newDate);

        return formattedDate;
    }

    public static String cutMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);

        Date newDate = calendar.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(newDate);

        return formattedDate;
    }

}
