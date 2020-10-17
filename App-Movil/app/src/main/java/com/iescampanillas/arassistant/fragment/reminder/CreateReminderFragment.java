package com.iescampanillas.arassistant.fragment.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
    private EditText reminderTitle;
    private EditText reminderDescription;
    private TimePicker reminderTimePicker;

    //Reminder
    private Reminder reminder;

    //Date
    private long dateInMilliseconds;

    //Time selected
    private int hourSelected, minuteSelected;

    //Boolean
    private boolean isUpdate;

    public CreateReminderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createReminderView = inflater.inflate(R.layout.fragment_create_reminder, container, false);

        //Bind elements
        reminderTitle = createReminderView.findViewById(R.id.fragmentCreateReminderTitleText);
        reminderDescription = createReminderView.findViewById(R.id.fragmentCreateReminderDescText);
        reminderTimePicker = createReminderView.findViewById(R.id.fragmentCreateReminderTimePicker);
        Button btnReturn = createReminderView.findViewById(R.id.fragmentCreateReminderReturnButton);
        Button btnSave = createReminderView.findViewById(R.id.fragmentCreateReminderSaveButton);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();


        //Set time picker mode
        reminderTimePicker.setIs24HourView(true);

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

        //Show keyboard
        KeyboardUtils.showKeyboard(getActivity());

        //Set focus
        reminderTitle.setFocusable(true);
        reminderTitle.requestFocus();


        //Time picker
        setHourAndMinute(reminderTimePicker.getHour(), reminderTimePicker.getMinute());
        reminderTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) ->
                setHourAndMinute(hourOfDay, minute));

        //Return button
        btnReturn.setOnClickListener(v -> findNavController(v).navigateUp());

        //Save button
        btnSave.setOnClickListener(this::saveReminder);

        return createReminderView;
    }

    /**
     * Save the selected hour and minute
     *
     * @param h The hour to be stored in 'hourSelected'
     * @param m The minute to be stored in 'minuteSelected'
     * */
    private void setHourAndMinute(int h, int m) {
        hourSelected = h;
        minuteSelected = m;
    }

    /**
     * Set in milliseconds the date and time selected
     *
     * @param isUpdate Boolean to see if a reminder is a new one or not
     * */
    private void setDateTime(boolean isUpdate) {
        if (isUpdate) {
            Calendar dateToUpdate = new GregorianCalendar();
            dateToUpdate.setTimeInMillis(reminder.getDateTime());
            dateToUpdate.set(Calendar.HOUR_OF_DAY, hourSelected);
            dateToUpdate.set(Calendar.MINUTE, minuteSelected);
            dateInMilliseconds = dateToUpdate.getTimeInMillis();
        } else {
            Calendar date = (Calendar) getArguments().get(AppString.DATE_SELECTED);
            date.set(Calendar.HOUR_OF_DAY, hourSelected);
            date.set(Calendar.MINUTE, minuteSelected);
            dateInMilliseconds = date.getTimeInMillis();
        }
    }

    /**
     * Save the reminder in the database
     *
     * @param v The actual view
     * */
    private void saveReminder(View v) {
        //Check title
        if(reminderTitle.getText().toString().isEmpty()) {
            reminderTitle.setError(getString(R.string.error_empty_fields));
        } else {
            //Get the data
            String title = reminderTitle.getText().toString();
            String desc = reminderDescription.getText().toString();
            int h = reminderTimePicker.getHour();
            int m = reminderTimePicker.getMinute();
            setHourAndMinute(h, m);
            setDateTime(isUpdate);

            //Set the data in the reminder
            reminder.setTitle(title);
            reminder.setDescription(desc);
            reminder.setDate(dateInMilliseconds);

            //Get the database reference, uid and the current date
            DatabaseReference dbRef = fbDatabase.getReference();
            String uid = fbAuth.getCurrentUser().getUid();
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.SECOND, 0);
            long currentDateMs = currentDate.getTimeInMillis();

            //Check if the selected date if in the past
            if(currentDateMs < dateInMilliseconds) {

                //Save the data or update
                if (isUpdate) {
                    //Update
                    HashMap<String, Object> reminderUpdate = new HashMap<>();
                    reminderUpdate.put(AppString.DB_REMINDER_REF + uid + "/" + reminder.getId(), reminder);
                    dbRef.updateChildren(reminderUpdate).addOnSuccessListener(aVoid -> {
                        Toast.makeText(v.getContext(), R.string.toast_update_reminder_success, Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(v.getContext(), R.string.toast_update_reminder_error, Toast.LENGTH_LONG).show();
                    }).addOnCompleteListener(task -> findNavController(v).navigateUp());
                } else {
                    //Generate id
                    String reminderId = Generator.generateId(AppString.REMINDER_PREFIX);
                    reminder.setId(reminderId);
                    //Save tha data
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
            } else {
                Toast.makeText(v.getContext(), R.string.toast_no_past_reminders, Toast.LENGTH_LONG).show();
            }
        }

    }
}
