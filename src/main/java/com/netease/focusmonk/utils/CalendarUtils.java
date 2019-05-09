package com.netease.focusmonk.utils;

import java.util.Calendar;

public final class CalendarUtils {
    public static final int MILLI_2_SECOND = 1000;
    public static final int SECOND_2_MINUTE = 60;

    public static boolean isSameDay(Calendar c1, Calendar c2) {
        return c1.get(Calendar.ERA) == c2.get(Calendar.ERA) &&
               c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
               c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);

    }


}
