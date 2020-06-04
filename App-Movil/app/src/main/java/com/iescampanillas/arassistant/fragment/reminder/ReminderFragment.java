package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.adapter.reminder.ReminderAdapter;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;
import com.iescampanillas.arassistant.utils.DateTimeUtils;
import com.iescampanillas.arassistant.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import static androidx.navigation.Navigation.findNavController;

public class ReminderFragment extends Fragment {

    //Arrays
    private ArrayList<Reminder> reminderList;
    private ArrayList<EventDay> eventDays;

    //Reminder
    private ReminderAdapter reminderAdapter;

    //CalendarView
    private CalendarView calendar;

    //Calendar
    private Calendar date;

    //Bundle
    private Bundle dateBundle;

    public ReminderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View reminderView = inflater.inflate(R.layout.fragment_reminder, container, false);

        //Hide keyboard
        KeyboardUtils.hideKeyboard(getActivity());

        //Bind elements
        calendar = reminderView.findViewById(R.id.reminderFragmentCalendar);
        Button btnCreateReminder = reminderView.findViewById(R.id.fragmentNewReminderButton);

        //Initialize bundle and calendar
        dateBundle = new Bundle();
        date = GregorianCalendar.getInstance();

        //Calendar listener
        calendar.setOnDayClickListener(eventDay -> {
            date.setTimeInMillis(eventDay.getCalendar().getTimeInMillis());
            getData(reminderView, date.getTimeInMillis());
        });

        //Create reminder button
        btnCreateReminder.setOnClickListener(v -> {
            //Check if selected date is past
            if(DateTimeUtils.isPast(date)) {
                Toast.makeText(v.getContext(), R.string.toast_no_past_reminders, Toast.LENGTH_LONG).show();
            } else {
                //Send the selected date to create reminder fragment
                dateBundle.putSerializable(AppString.DATE_SELECTED, date);
                findNavController(v).navigate(R.id.reminder_to_createReminder, dateBundle);
            }
        });

        //Get the reminders from database
        getData(reminderView, date.getTimeInMillis());

        return reminderView;
    }

    /**
     * Get the data from database
     *
     * @param v The catual view
     * @param selectedDate The selected date to show the reminders of the day
     * */
    private void getData(View v, long selectedDate) {
        //Adapter
        reminderAdapter = new ReminderAdapter(getActivity());
        //Recycler
        RecyclerView reminderRecycler = v.findViewById(R.id.fragmentReminderRecycler);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderRecycler.setAdapter(reminderAdapter);

        //Arrays
        reminderList = new ArrayList<>();
        eventDays = new ArrayList<>();

        //Firebase
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        DatabaseReference reminderRef = fbDatabase.getReference(AppString.DB_REMINDER_REF + uid);
        reminderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnap: dataSnapshot.getChildren()) {
                    Reminder reminder = dSnap.getValue(Reminder.class);

                    //Check the date of the reminder to mark the calendar
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(reminder.getDateTime());
                    eventDays.add(new EventDay(cal, R.drawable.ic_mark));
                    //Check if the reminder is for the selected date
                    if(DateTimeUtils.checkReminder(selectedDate, reminder.getDateTime())) {
                        //Check if the reminder is already added to the list
                        AtomicBoolean reminderAlreadyAdded = new AtomicBoolean(false);
                        reminderList.forEach(reminder1 -> {
                            if(reminder.getId().equals(reminder1.getId())) {
                                reminderAlreadyAdded.set(true);
                            }
                        });
                        if(!reminderAlreadyAdded.get()){
                            reminderList.add(reminder);
                        }
                    }
                }
                //Mark calendar days with reminders
                calendar.setEvents(eventDays);
                //Send the data to the adapter
                reminderAdapter.setData(reminderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.toast_get_reminder_error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
