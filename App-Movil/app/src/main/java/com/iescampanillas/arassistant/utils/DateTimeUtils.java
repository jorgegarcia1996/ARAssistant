package com.iescampanillas.arassistant.utils;

import com.iescampanillas.arassistant.constant.AppString;

import java.text.SimpleDateFormat;
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
    public static String getFormatDate(long milliSeconds){
        SimpleDateFormat formatter = new SimpleDateFormat(AppString.DATE_FORMAT, Locale.getDefault());
        return formatter.format(milliSeconds);
    }
}
