package com.iescampanillas.arassistant.adapter.reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;
import com.iescampanillas.arassistant.utils.DateTimeUtils;

import java.util.ArrayList;

import static androidx.navigation.Navigation.findNavController;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderHolder> {

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDatabase;

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
    }

    @Override
    public int getItemCount() {return this.data.size();}

    public class ReminderHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView time, title;
        private Reminder reminder;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reminderItemTitle);
            time = itemView.findViewById(R.id.reminderItemTime);
            title.setOnCreateContextMenuListener(this);
            time.setOnCreateContextMenuListener(this);
        }

        public void BindHolder(Reminder reminder) {
            this.reminder = reminder;
            title.setText(reminder.getTitle());
            time.setText(DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.TIME_FORMAT));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem seeDetails = menu.add(Menu.NONE, 1, 1, R.string.label_see_dialog);
            MenuItem edit = menu.add(Menu.NONE, 2, 2, R.string.label_edit_dialog);
            MenuItem delete = menu.add(Menu.NONE, 3, 3, R.string.label_delete_dialog);
            edit.setOnMenuItemClickListener(item -> {
               editReminder(v, reminder);
                return false;
            });
            delete.setOnMenuItemClickListener(item -> {
                deleteReminder(reminder);
                return false;
            });
            seeDetails.setOnMenuItemClickListener(item -> {
                seeDetailsReminder(v, reminder);
                return false;
            });
        }

    }
    //Custom methods
    private void editReminder(View v, Reminder reminder) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppString.EDIT_REMINDER, reminder);
        findNavController(v).navigate(R.id.reminder_to_createReminder, bundle);
    }


    private void deleteReminder(Reminder reminder) {
        data.clear();
        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        fbDatabase.getReference(AppString.DB_REMINDER_REF).child(uid).child(reminder.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ctx, R.string.toast_delete_reminder, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(ctx, R.string.toast_delete_reminder_error, Toast.LENGTH_SHORT).show();
                });

    }

    private void seeDetailsReminder(View v,Reminder reminder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.TIME_FORMAT)
                + " " + reminder.getTitle());
        builder.setMessage(reminder.getDescription());
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.label_close_dialog, (dialog, which) -> dialog.cancel());
        AlertDialog taskAlert = builder.create();
        taskAlert.show();
    }
}
