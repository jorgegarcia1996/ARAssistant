package com.iescampanillas.arassistant.fragment;

import android.accessibilityservice.AccessibilityService;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.Generator;
import com.iescampanillas.arassistant.utils.KeyboardUtils;
import com.iescampanillas.arassistant.utils.UserUtils;

import java.util.HashMap;

import static androidx.navigation.Navigation.findNavController;

public class CreateTaskFragment extends Fragment {

    //Firebase
    private FirebaseDatabase fbDatabase;

    //Task
    private Task task;

    //Boolean
    private Boolean isUpdate;

    //Inputs
    private EditText taskTitle, taskDescription;

    //Toolbar
    private Toolbar toolbar;

    //Save button
    private Button saveTask;

    public CreateTaskFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Show keyboard
        KeyboardUtils.showKeyboard(getActivity());
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

        //Check if are any arguments for create new task or update
        if(getArguments() != null) {
            //Get arguments
            task = (Task) getArguments().get(AppString.TASK_TO_EDIT);
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            isUpdate = true;
        } else {
            //Create new task
            task = new Task();
            isUpdate = false;
        }

        //Set focus
        taskTitle.setFocusable(true);
        taskTitle.requestFocus();

        //Back button
        toolbar.setNavigationOnClickListener(v -> findNavController(v).navigate(R.id.createTask_to_task));

        //Save button
        saveTask.setOnClickListener(this::saveTask);

        return createTaskView;
    }

    /**
     * Method to save the task data in firebase
     *
     * @param v is the current view
     * */
    private void saveTask(View v) {
        //Check if title input is empty
        if (taskTitle.getText().toString().equals("")) {
            //Empty
            taskTitle.setError(getString(R.string.error_empty_fields));
        } else {
            //Get the data if not empty
            task.setTitle(taskTitle.getText().toString());
            task.setDescription(taskDescription.getText().toString());

            //Get UID for link the task to user
            String uid = new UserUtils().getCurrentUserUid();

            //Database reference
            DatabaseReference dbRef = fbDatabase.getReference();

            //Check if is an update or create a new task
            if (isUpdate) {
                //Update the task in Firebase
                HashMap<String, Object> taskUpdate = new HashMap<>();
                taskUpdate.put(AppString.DB_TASK_REF + uid + "/" + task.getId(), task);
                dbRef.updateChildren(taskUpdate).addOnSuccessListener(aVoid -> {
                    //Update Success
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_update_task_success, Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    //Update Failed
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_update_task_error, Toast.LENGTH_LONG).show();
                });
                //Return to Task list
                findNavController(v).navigate(R.id.createTask_to_task);
            } else {
                //New task
                //Generate task Id
                String taskId = new Generator().generateId(AppString.TASK_PREFIX);
                task.setId(taskId);
                //Create the task in Firebase
                dbRef.child(AppString.DB_TASK_REF).child(uid).child(taskId).setValue(task)
                        .addOnSuccessListener(aVoid -> {
                            //Created Successfully
                            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_create_task_success, Toast.LENGTH_LONG).show();
                            taskTitle.getText().clear();
                            taskDescription.getText().clear();
                            //Set focus
                            taskTitle.setFocusable(true);
                            taskTitle.requestFocus();
                        }).addOnFailureListener(e -> {
                            //Failure in creation process
                            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_create_task_error, Toast.LENGTH_LONG).show();
                        });
            }
        }
    }
}
