package vn.prostylee.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {}

    public static String toDateTimeString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }

    public static Date getLastDaysBefore(int numberOfDays) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, (-1) * numberOfDays);
        return today.getTime();
    }
}
