package com.xtree.live.uitl;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    private static ThreadLocal<SimpleDateFormat> dateTimeFormat = new ThreadLocal<>();
    public static Long getDateToTimestamp(String dateString) {
        long time = 0L;
        try {
            if (!TextUtils.isEmpty(dateString)) {
                SimpleDateFormat format = dateTimeFormat.get();
                if(format == null){
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    dateTimeFormat.set(format);
                }
                Date date = format.parse(dateString);
                if (date != null) {
                    time = date.getTime() / 1000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getHourAndMinuteFromTimestamp(Long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(new Date(timestamp * 1000));
    }

    public static Boolean isSameDay(Long oldTimestamp, Long newTimestamp) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(oldTimestamp * 1000);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(newTimestamp * 1000);
        return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthAndDayFromTimestamp(Long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM - dd", Locale.getDefault());
        return formatter.format(new Date(timestamp * 1000));
    }

    public static String getCurrentTime() {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static int getCurrentDays(long timeDistance) {

        return (int) (timeDistance / (24 * 60 * 60 * 1000));

    }

    public static int getCurrentHour(long timeDistance) {

        int hour = (int) (timeDistance % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);

        return hour;

    }

    public static int getCurrentMinute(long timeDistance) {

        int minute = 0;

        minute = (int) (timeDistance % (24 * 60 * 60 * 1000)) % (60 * 60 * 1000) / (60 * 1000);

        return minute;

    }

    public static int getCurrentMills(long timeDistance) {

        int mills = (int) (timeDistance % (24 * 60 * 60 * 1000)) % (60 * 60 * 1000) % (60 * 1000) / 1000;

        return mills;

    }
}

