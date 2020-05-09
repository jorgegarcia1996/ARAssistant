package com.iescampanillas.arassistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.adapter.TaskAdapter;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.Generator;
import com.iescampanillas.arassistant.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;

public class TaskFragment extends Fragment {

    //FBDatabase
    private FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;
    //Array
    private ArrayList<Task> tasksList;

    //Adapter
    private TaskAdapter taskAdapter;

    //Spinner filter
    private Spinner spinnerFilter;

    //Button New Task
    private Button btnNewTask;

    //Recycler
    private RecyclerView tasksRecycler;

    public TaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View taskView = inflater.inflate(R.layout.fragment_task, container, false);

        //Hide keyboard
        KeyboardUtils.hideKeyboard(getActivity());

        //NewTask button
        btnNewTask = taskView.findViewById(R.id.fragmentNewTaskButton);
        btnNewTask.setOnClickListener(v -> {
            findNavController(v).navigate(R.id.task_to_createTask);
        });

        //Filter
        spinnerFilter = taskView.findViewById(R.id.fragmentTaskSpinnerCategoryFilter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cat = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    cat = "All";
                }
                //Get the data from Firebase
                getData(taskView, cat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return taskView;
    }

    private void getData(View v, String category) {
        taskAdapter = new TaskAdapter(getActivity());

        //Recycler
        tasksRecycler = v.findViewById(R.id.fragmentTaskRecycler);
        tasksRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasksRecycler.setAdapter(taskAdapter);

        //Start array
        tasksList = new ArrayList<>();

        //Get the data from database and send it to the adapter
        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        DatabaseReference tasksRef = fbDatabase.getReference(AppString.DB_TASK_REF + uid);
        tasksRef.addValueEventListener(new ValueEventListener() {
            //Get data
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get every task and save it into the array
                for (DataSnapshot dSnap: dataSnapshot.getChildren()) {
                    Task task = dSnap.getValue(Task.class);

                    if (task.getCategory().equals(category) || category.equals("All")) {
                        tasksList.add(task);
                    }
                }
                //Send the array to the adapter
                taskAdapter.setData(tasksList);
            }

            //Error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.toast_get_task_error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
