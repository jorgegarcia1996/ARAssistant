package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;
import com.iescampanillas.arassistant.utils.DateTimeUtils;
import com.iescampanillas.arassistant.utils.Generator;
import com.iescampanillas.arassistant.utils.KeyboardUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static androidx.navigation.Navigation.findNavController;

public class CreateReminderFragment extends Fragment {

    //Firebase
    private FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;

    //Inputs
    private EditText reminderTitle, reminderDescription;
    private TimePicker reminderTimePicker;

    //Buttons
    private Button btnReturn, btnSave;

    //Reminder
    private Reminder reminder;

    //Date
    private long dateInMilliseconds;

    //Time
    private int hourSelected, minuteSelected;

    //Boolean
    private boolean isUpdate;

    public CreateReminderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createReminderView = inflater.inflate(R.layout.fragment_create_reminder, container, false);

        //Bind Elements
        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();
        reminderTitle = createReminderView.findViewById(R.id.fragmentCreateReminderTitleText);
        reminderDescription = createReminderView.findViewById(R.id.fragmentCreateReminderDescText);
        reminderTimePicker = createReminderView.findViewById(R.id.fragmentCreateReminderTimePicker);
        btnReturn = createReminderView.findViewById(R.id.fragmentCreateReminderReturnButton);
        btnSave = createReminderView.findViewById(R.id.fragmentCreateReminderSaveButton);

        //Create Reminder or Edit
        if(getArguments().get(AppString.EDIT_REMINDER) != null) {
            //Get arguments
            reminder = (Reminder) getArguments().get(AppString.EDIT_REMINDER);
            reminderTitle.setText(reminder.getTitle());
            reminderDescription.setText(reminder.getDescription());
            reminderTimePicker.setHour(DateTimeUtils.getDateElement(reminder.getDateTime(), AppString.HOUR));
            reminderTimePicker.setMinute(DateTimeUtils.getDateElement(reminder.getDateTime(), AppString.MINUTE));
            isUpdate = true;
        } else {
            reminder = new Reminder();
            isUpdate = false;
        }

        //Set focus
        reminderTitle.setFocusable(true);
        reminderTitle.requestFocus();

        //Show keyboard
        KeyboardUtils.showKeyboard(getActivity());

        //Time picker
        reminderTimePicker.setIs24HourView(true);
        setHourAndMinute(reminderTimePicker.getHour(), reminderTimePicker.getMinute());
        reminderTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) ->
                setHourAndMinute(hourOfDay, minute));

        btnReturn.setOnClickListener(v -> findNavController(v).navigateUp());

        btnSave.setOnClickListener(this::saveReminder);

        return createReminderView;
    }

    private void setHourAndMinute(int h, int m) {
        hourSelected = h;
        minuteSelected = m;
    }

    private void setDateTime(boolean isUpdate) {
        if (isUpdate) {
            Calendar dateToUpdate = new GregorianCalendar();
            dateToUpdate.setTimeInMillis(reminder.getDateTime());
            dateToUpdate.set(Calendar.HOUR, hourSelected);
            dateToUpdate.set(Calendar.MINUTE, minuteSelected);
            dateInMilliseconds = dateToUpdate.getTimeInMillis();
        } else {
            Calendar date = (Calendar) getArguments().get(AppString.DATE_SELECTED);
            date.set(Calendar.HOUR, hourSelected);
            date.set(Calendar.MINUTE, minuteSelected);
            dateInMilliseconds = date.getTimeInMillis();
        }
    }

    private void saveReminder(View v) {
        if(reminderTitle.getText().toString().isEmpty()) {
            reminderTitle.setError(getString(R.string.error_empty_fields));
        } else {
            String title = reminderTitle.getText().toString();
            String desc = reminderDescription.getText().toString();
            int h = reminderTimePicker.getHour();
            int m = reminderTimePicker.getMinute();
            setHourAndMinute(h, m);
            setDateTime(isUpdate);
            reminder.setTitle(title);
            reminder.setDescription(desc);
            reminder.setDate(dateInMilliseconds);

            DatabaseReference dbRef = fbDatabase.getReference();
            String uid = fbAuth.getCurrentUser().getUid();

            if(isUpdate) {
                HashMap<String, Object> reminderUpdate = new HashMap<>();
                reminderUpdate.put(AppString.DB_REMINDER_REF + uid + "/" + reminder.getId(), reminder);
                dbRef.updateChildren(reminderUpdate).addOnSuccessListener(aVoid -> {
                    Toast.makeText(v.getContext(), R.string.toast_update_reminder_success, Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), R.string.toast_update_reminder_error, Toast.LENGTH_LONG).show();
                }).addOnCompleteListener(task -> findNavController(v).navigateUp());
            } else {
                String reminderId = Generator.generateId(AppString.REMINDER_PREFIX);
                reminder.setId(reminderId);
                dbRef.child(AppString.DB_REMINDER_REF).child(uid).child(reminderId).setValue(reminder)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(v.getContext(), R.string.toast_create_reminder_success, Toast.LENGTH_LONG).show();
                            reminderTitle.getText().clear();
                            reminderDescription.getText().clear();
                            reminderTitle.setFocusable(true);
                            reminderTitle.requestFocus();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), R.string.toast_create_reminder_error, Toast.LENGTH_LONG).show();
                });
            }
        }

    }
}
