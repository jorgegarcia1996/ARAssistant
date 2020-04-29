package com.iescampanillas.arassistant.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

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
        holder.BindHolder(data.get(pos));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView title;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskItemTitle);
        }

        public void BindHolder(Task task) {
            title.setText(task.getTitle());
        }

        //Menu contextual
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
