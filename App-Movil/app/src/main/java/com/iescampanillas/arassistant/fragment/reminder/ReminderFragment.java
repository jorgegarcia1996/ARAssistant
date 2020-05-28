package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.utils.DateTimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ReminderFragment extends Fragment {

    private CalendarView calendar;
    private TimePicker timePicker;

    private int hour, min;

    public ReminderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View reminderView = inflater.inflate(R.layout.fragment_reminder, container, false);

        calendar = reminderView.findViewById(R.id.reminderFragmentCalendar);
        timePicker = reminderView.findViewById(R.id.timePicker);

        timePicker.setIs24HourView(true);

        hour = timePicker.getHour();
        min = timePicker.getMinute();

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hour = hourOfDay;
            min = minute;
        });

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            long selectedDateInMilliseconds = new GregorianCalendar(year, month,dayOfMonth, hour, min).getTimeInMillis();
            String date = DateTimeUtils.getFormatDate(selectedDateInMilliseconds);
            Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
        });


        return reminderView;
    }
}
