package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import static androidx.navigation.Navigation.findNavController;

public class ReminderFragment extends Fragment {

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDatabase;

    private ArrayList<Reminder> reminderList;

    private ReminderAdapter reminderAdapter;

    private RecyclerView reminderRecycler;

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
            getData(reminderView, date.getTimeInMillis());
        });

        btnCreateReminder.setOnClickListener(v -> {
            if(DateTimeUtils.isPast(date)) {
                Toast.makeText(v.getContext(), R.string.toast_no_past_reminders, Toast.LENGTH_LONG).show();
            } else {
                dateBundle.putSerializable(AppString.DATE_SELECTED, date);
                findNavController(v).navigate(R.id.reminder_to_createReminder, dateBundle);
            }
        });

        getData(reminderView, date.getTimeInMillis());

        return reminderView;
    }

    private void getData(View v, long selectedDate) {
        reminderAdapter = new ReminderAdapter(getActivity());

        reminderRecycler = v.findViewById(R.id.fragmentReminderRecycler);
        reminderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderRecycler.setAdapter(reminderAdapter);

        reminderList = new ArrayList<>();

        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        DatabaseReference reminderRef = fbDatabase.getReference(AppString.DB_REMINDER_REF + uid);
        reminderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnap: dataSnapshot.getChildren()) {
                    Reminder reminder = dSnap.getValue(Reminder.class);
                    if(DateTimeUtils.checkReminder(date.getTimeInMillis(), reminder.getDateTime())) {
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
                reminderAdapter.setData(reminderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.toast_get_reminder_error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
