package com.iescampanillas.arassistant.utils;

import com.iescampanillas.arassistant.constant.AppString;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimeUtils {

    private static DateTimeUtils dateTimeUtilsInstance = null;

    private DateTimeUtils() {
    }

    public static DateTimeUtils getInstance() {
        if(dateTimeUtilsInstance == null) {
            dateTimeUtilsInstance = new DateTimeUtils();
        }
        return dateTimeUtilsInstance;
    }


    /**
     * Formatter of the date given in milliseconds
     *
     * */
    public static String getFormatDate(long milliSeconds, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(milliSeconds);
    }

    /**
     * Get a element (day of month, month, year, hour or minute) of the given date in milliseconds
     * */
    public static int getDateElement(long milliseconds, String element) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int value;
        switch (element) {
            case AppString.YEAR:
                value = calendar.get(Calendar.YEAR);
                break;
            case AppString.MONTH:
                value = calendar.get(Calendar.MONTH);
                break;
            case AppString.DAY_OF_MONTH:
                value = calendar.get(Calendar.DAY_OF_MONTH);
                break;
            case AppString.HOUR:
                value = calendar.get(Calendar.HOUR);
                break;
            case AppString.MINUTE:
                value = calendar.get(Calendar.MINUTE);
                break;
            default:
                value = Integer.MAX_VALUE;
        }
        return value;
    }
}
