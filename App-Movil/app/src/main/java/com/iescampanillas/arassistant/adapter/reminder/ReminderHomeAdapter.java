package com.iescampanillas.arassistant.adapter.reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;
import com.iescampanillas.arassistant.utils.DateTimeUtils;

import java.util.ArrayList;

import static androidx.navigation.Navigation.findNavController;

public class ReminderHomeAdapter extends RecyclerView.Adapter<ReminderHomeAdapter.ReminderHomeHolder> {

    //Array, index and context
    private ArrayList<Reminder> data;
    private int index;
    private Context ctx;

    public ReminderHomeAdapter(Context context) {
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
    public ReminderHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                    .inflate(R.layout.layout_reminder_item, parent, false);
        ReminderHomeHolder rh = new ReminderHomeHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderHomeHolder holder, int pos) {
        Reminder reminder = data.get(pos);
        holder.BindHolder(reminder);
        //Open dialog
        holder.time.setOnClickListener(v -> showReminderAlertDialog(v, reminder));
        holder.title.setOnClickListener(v -> showReminderAlertDialog(v, reminder));
    }

    @Override
    public int getItemCount() {return this.data.size();}

    public class ReminderHomeHolder extends RecyclerView.ViewHolder {
        //Elements of each item
        private TextView time, title;

        public ReminderHomeHolder(@NonNull View itemView) {
            super(itemView);
            //Bind elements
            title = itemView.findViewById(R.id.reminderItemTitle);
            time = itemView.findViewById(R.id.reminderItemTime);
        }

        public void BindHolder(Reminder reminder) {
            //Set the content of each element
            title.setText(reminder.getTitle());
            time.setText(DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.DATE_FORMAT));
        }
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
                + " " +  DateTimeUtils.getFormatDate(reminder.getDateTime(), AppString.TIME_FORMAT)
                + "\n\n" +  reminder.getTitle());
        builder.setMessage(reminder.getDescription());
        builder.setCancelable(false);
        //Close button
        builder.setPositiveButton(R.string.label_close_dialog, (dialog, which) -> dialog.cancel());
        AlertDialog reminderAlert = builder.create();
        reminderAlert.show();
    }
}
