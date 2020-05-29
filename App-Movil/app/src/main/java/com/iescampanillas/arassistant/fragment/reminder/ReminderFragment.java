package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.utils.DateTimeUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static androidx.navigation.Navigation.findNavController;

public class ReminderFragment extends Fragment {

    private CalendarView calendar;

    private Calendar date;

    private Button btnCreateReminder;

    private Bundle dateBundle;

    public ReminderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View reminderView = inflater.inflate(R.layout.fragment_reminder, container, false);

        calendar = reminderView.findViewById(R.id.reminderFragmentCalendar);
        btnCreateReminder = reminderView.findViewById(R.id.fragmentNewReminderButton);

        dateBundle = new Bundle();
        date = GregorianCalendar.getInstance();

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            date.set(year, month,dayOfMonth);
        });

        btnCreateReminder.setOnClickListener(v -> {
            dateBundle.putSerializable(AppString.DATE_SELECTED, date);
            findNavController(v).navigate(R.id.reminder_to_createReminder, dateBundle);
        });

        return reminderView;
    }
}
