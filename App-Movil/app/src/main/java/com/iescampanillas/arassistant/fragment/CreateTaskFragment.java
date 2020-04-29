package com.iescampanillas.arassistant.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.Generator;

import static androidx.navigation.Navigation.findNavController;

public class CreateTaskFragment extends Fragment {

    //Firebase
    private FirebaseDatabase fbDatabase;

    //Task
    private Task task;

    //Inputs
    private EditText taskTitle, taskDescription;

    //Toolbar
    private Toolbar toolbar;

    //Save button
    private Button saveTask;

    public CreateTaskFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createTaskView = inflater.inflate(R.layout.fragment_create_task, container, false);

        //Bind elements
        fbDatabase = FirebaseDatabase.getInstance();
        toolbar = createTaskView.findViewById(R.id.fragmentCreateTaskToolbar);
        taskTitle = createTaskView.findViewById(R.id.fragmentCreateTaskTitleText);
        taskDescription = createTaskView.findViewById(R.id.fragmentCreateTaskDescText);
        saveTask = createTaskView.findViewById(R.id.fragmentCreateTaskConfirmBtn);

        task = new Task();

        //Back button
        toolbar.setNavigationOnClickListener(v -> findNavController(v).navigate(R.id.createTask_to_task));

        saveTask.setOnClickListener(v -> {
            //Generate task Id
            String taskId = new Generator().generateId(AppString.taskPrefix);
            //Save Task object
            task.setTitle(taskTitle.getText().toString());
            task.setDescription(taskDescription.getText().toString());

            fbDatabase.getReference().child("task").child(taskId).setValue(task).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_create_task_success, Toast.LENGTH_LONG).show();
                    taskTitle.getText().clear();
                    taskDescription.getText().clear();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_create_task_error, Toast.LENGTH_LONG).show();
                }
            });
        });

        return createTaskView;
    }
}
