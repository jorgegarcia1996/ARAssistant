package com.iescampanillas.arassistant.adapter.task;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.model.Task;

import java.util.ArrayList;

public class TaskHomeAdapter extends RecyclerView.Adapter<TaskHomeAdapter.TaskHomeHolder> {

    //Data and context
    private ArrayList<Task> data;
    private int index;
    private Context ctx;

    public TaskHomeAdapter(Context context) {
        this.data = new ArrayList<>();
        this.ctx = context;
    }

    public void setData(ArrayList<Task> sdata) {
        this.data = sdata;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return this.index;
    }

    @NonNull
    @Override
    public TaskHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.layout_task_item, parent, false);

        TaskHomeHolder th = new TaskHomeHolder(v);
        return th;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHomeHolder holder, int pos) {
        Task task = data.get(pos);
        holder.BindHolder(task);
        //Event to show dialog with the details
        holder.title.setOnClickListener(v -> showTaskAlertDialog(v, task));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class TaskHomeHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public TaskHomeHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskItemTitle);
        }

        public void BindHolder(Task task) {
            title.setText(task.getTitle());
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(task.getIcon(), 0, 0, 0);
        }
    }

    //Custom methods
    /**
     * Create an alert dialog with the data of the task with 1 button:
     * Close: Close the dialog.
     *
     * @param view The view where show the dialog
     * @param task The task to make an action
     *
     * */
    private void showTaskAlertDialog(View view, Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(task.getTitle());
        builder.setMessage(task.getDescription() + "\n\n" + task.getCategory() + "\n\n" + task.getMedia());
        builder.setCancelable(false);
        //Close button
        builder.setPositiveButton(R.string.label_close_dialog, (dialog, which) -> dialog.cancel());
        AlertDialog taskAlert = builder.create();
        taskAlert.show();
    }
}
