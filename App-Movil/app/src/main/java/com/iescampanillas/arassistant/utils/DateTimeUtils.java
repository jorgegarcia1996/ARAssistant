package com.iescampanillas.arassistant.utils;

import com.iescampanillas.arassistant.constant.AppString;

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
     * @param milliSeconds The date in milliseconds to be formatted
     * @param format The format of the date
     * */
    public static String getFormatDate(long milliSeconds, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(milliSeconds);
    }

    /**
     * Get a element (day of month, month, year, hour or minute) of the given date in milliseconds
     *
     * @param milliseconds The date in milliseconds to get an element
     * @param element Year, month, day of month, hour of day or minute.
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
                value = calendar.get(Calendar.HOUR_OF_DAY);
                break;
            case AppString.MINUTE:
                value = calendar.get(Calendar.MINUTE);
                break;
            default:
                value = Integer.MAX_VALUE;
        }
        return value;
    }

    /**
     * Check if the given date is past
     *
     * @param date The date to check if is past
     * */
    public static boolean isPast(Calendar date) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        long currentDateMs = currentDate.getTimeInMillis();
        long selectedDateMs = date.getTimeInMillis();
        //Simplified if statement
        return currentDateMs > selectedDateMs;
    }

    /**
     * Check if the reminder if is for the given date
     *
     * @param selectedDate The day selected in the calendar in milliseconds
     * @param reminderDate The reminder date given in milliseconds
     * */
    public static boolean checkReminder(long selectedDate, long reminderDate) {
        //Get elements of the selected date
        int selectedDay = getDateElement(selectedDate, AppString.DAY_OF_MONTH);
        int selectedMonth = getDateElement(selectedDate, AppString.MONTH);
        int selectedYear = getDateElement(selectedDate, AppString.YEAR);

        //Get elements of the reminder date
        int reminderDay = getDateElement(reminderDate, AppString.DAY_OF_MONTH);
        int reminderMonth = getDateElement(reminderDate, AppString.MONTH);
        int reminderYear = getDateElement(reminderDate, AppString.YEAR);

        //Compare the dates
        Calendar selectedCalendar = new GregorianCalendar();
        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
        Calendar reminderCalendar = new GregorianCalendar();
        reminderCalendar.set(reminderYear, reminderMonth, reminderDay);
        return selectedCalendar.getTimeInMillis() == reminderCalendar.getTimeInMillis();
    }
}
