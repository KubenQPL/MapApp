package pl.jakubneukirch.mapapp.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtils {
    private static SimpleDateFormat dateFormat = null;
    private static Date date = null;

    public static String getStringDate(long timestamp) {
        getDate().setTime(timestamp);
        return getDateFormat().format(getDate());
    }

    private static SimpleDateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return dateFormat;
    }

    public static Date getDate() {
        if (date == null) {
            date = new Date();
        }
        return date;
    }
}
