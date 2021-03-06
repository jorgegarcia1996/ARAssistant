package com.iescampanillas.arassistant.adapter.reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;
import com.iescampanillas.arassistant.utils.DateTimeUtils;

import java.util.ArrayList;

import static androidx.navigation.Navigation.findNavController;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderHolder> {

    //Array, index and context
    private ArrayList<Reminder> data;
    private int index;
    private Context ctx;

    public ReminderAdapter(Context context) {
        this.data = new ArrayList<>();
        this.ctx = context;
    }

    public void setData(ArrayList<Reminder> sdata) {
        this.data = sdata;
        notifyDataSetChanged();
    }

    public int getIndex() {return this.index;}

    @NonNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                    .inflate(R.layout.layout_reminder_item, parent, false);
        ReminderHolder rh = new ReminderHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderHolder holder, int pos) {
        Reminder reminder = data.get(pos);
        holder.BindHolder(reminder);
        //Show Alert Dialog
        holder.title.setOnClickListener(v -> showReminderAlertDialog(v, reminder));
        holder.time.setOnClickListener(v -> showReminderAlertDialog(v, reminder));
    }

    @Override
    public int getItemCount() {return this.data.size();}

    public class ReminderHolder extends RecyclerView.ViewHolder {
        //Elements of each item
        private TextView time, title;
        private Reminder reminder;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            //Bind elements
            title = itemView.findViewById(R.id.reminderItemTitle);
            time = itemView.findViewById(R.id.reminderItemTime);
        }

        public void BindHolder(Reminder reminder) {
            //Set the content of each element
            this.reminder = reminder;
            title.setText(reminder.getTitle());
            time.setText(DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.TIME_FORMAT));
        }
    }

    //Custom methods
    /**
     * Send the data to 'create reminder fragment' to edit the reminder
     *
     * @param reminder The reminder to edit.
     * @param v The actual view.
     * */
    private void editReminder( Reminder reminder, View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppString.EDIT_REMINDER, reminder);
        findNavController(v).navigate(R.id.reminder_to_createReminder, bundle);
    }

    /**
     * Delete the reminder from list and database
     *
     * @param reminder The reminder to delete.
     * */
    private void deleteReminder(Reminder reminder) {
        data.clear();
        //Firebase
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();

        //Delete the reminder
        String uid = fbAuth.getCurrentUser().getUid();
        fbDatabase.getReference(AppString.DB_REMINDER_REF).child(uid).child(reminder.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ctx, R.string.toast_delete_reminder, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(ctx, R.string.toast_delete_reminder_error, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Show the reminder details in a dialog
     *
     * @param reminder The reminder to show the details
     * @param view The actual view
     * */
    private void showReminderAlertDialog(View view, Reminder reminder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.DATE_FORMAT)
                + " " + DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.TIME_FORMAT)
                + "\n\n" + reminder.getTitle());
        builder.setMessage(reminder.getDescription());
        builder.setCancelable(false);
        //Close button
        builder.setPositiveButton(R.string.label_close_dialog, (dialog, which) -> dialog.cancel());

        //Edit button
        builder.setNegativeButton(R.string.label_edit_dialog, (dialog, which) -> editReminder(reminder, view));
        //Delete button
        builder.setNeutralButton(R.string.label_delete_dialog, (dialog, which) -> deleteReminder(reminder));
        AlertDialog reminderAlert = builder.create();
        reminderAlert.show();
    }
}
