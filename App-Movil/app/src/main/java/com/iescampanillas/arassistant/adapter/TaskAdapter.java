package com.iescampanillas.arassistant.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
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
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.UserUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static androidx.navigation.Navigation.findNavController;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    //Firebase
    private FirebaseDatabase fbDatabase;

    //Data and context
    private ArrayList<Task> data;
    private int index;
    private Context ctx;

    public TaskAdapter(Context context) {
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
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.layout_task_item, parent, false);

        TaskHolder th = new TaskHolder(v);
        return th;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int pos) {
        Task task = data.get(pos);
        holder.BindHolder(task);
        //Event to show dialog with the details
        holder.title.setOnClickListener(v -> showTaskAlertDialog(v, task));
    }


    /**
     * Create an alert dialog with the data of the task with 3 buttons:
     * Close: Close the dialog.
     * Edit: Send the task data to CreateTaskFragment to edit the data and update it in firebase
     * Delete: Delete the task from firebase and update the task list
     *
     * @param task The task to make an action
     * @param view The view where show the dialog
     *
     * */
    private void showTaskAlertDialog(View view, Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(task.getTitle());
        builder.setMessage(task.getDescription());
        builder.setCancelable(false);
        //Close button
        builder.setPositiveButton(R.string.label_close_dialog, (dialog, which) -> dialog.cancel());
        //Edit button
        builder.setNegativeButton(R.string.label_edit_dialog, (dialog, which) -> editTask(task, view));
        //Delete button
        builder.setNeutralButton(R.string.label_delete_dialog, (dialog, which) -> deleteTask(task.getId()));
        AlertDialog taskAlert = builder.create();
        taskAlert.show();
    }

    /**
     * Method to send the task data to CreateTaskFragment by bundle to edit the task
     *
     * @param task The task to edit
     * @param view The actual view
     *
     * */
    private void editTask(Task task, View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppString.TASK_TO_EDIT, task);
        findNavController(view).navigate(R.id.task_to_createTask, bundle);
    }

    /**
     * Method to delete a task from firebase and update the list
     *
     * @param taskId Id of the task to delete
     *
     * */
    private void deleteTask(String taskId) {
        fbDatabase = FirebaseDatabase.getInstance();
        String uid = new UserUtils().getCurrentUserUid();
        fbDatabase.getReference(AppString.DB_TASK_REF).child(uid).child(taskId).removeValue();
        Toast.makeText(ctx, R.string.toast_deleted_task, Toast.LENGTH_SHORT).show();
        data.clear();
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskItemTitle);
        }

        public void BindHolder(Task task) {
            title.setText(task.getTitle());
        }

    }
}
