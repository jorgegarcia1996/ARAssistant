package com.iescampanillas.arassistant.utils;

import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    public static boolean isPast(Calendar date) {
        long currentDate = Calendar.getInstance().getTimeInMillis();
        long selectedDate = date.getTimeInMillis();
        //Simplified if statement
        return currentDate > selectedDate;
    }

    public static boolean checkReminder(long selectedDate, long reminderDate) {
        int selectedDay = getDateElement(selectedDate, AppString.DAY_OF_MONTH);
        int selectedMonth = getDateElement(selectedDate, AppString.MONTH);
        int selectedYear = getDateElement(selectedDate, AppString.YEAR);

        int reminderDay = getDateElement(reminderDate, AppString.DAY_OF_MONTH);
        int reminderMonth = getDateElement(reminderDate, AppString.MONTH);
        int reminderYear = getDateElement(reminderDate, AppString.YEAR);

        Calendar selectedCalendar = new GregorianCalendar();
        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

        Calendar reminderCalendar = new GregorianCalendar();
        reminderCalendar.set(reminderYear, reminderMonth, reminderDay);

        return selectedCalendar.getTimeInMillis() == reminderCalendar.getTimeInMillis();
    }
}
