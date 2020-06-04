package com.iescampanillas.arassistant.fragment.task;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.adapter.task.TaskAdapter;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.database.CategoriesContract;
import com.iescampanillas.arassistant.database.CategoriesDBHelper;
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static androidx.navigation.Navigation.findNavController;

public class TaskFragment extends Fragment {

    //Array
    private ArrayList<Task> tasksList;

    //Adapter
    private TaskAdapter taskAdapter;

    public TaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View taskView = inflater.inflate(R.layout.fragment_task, container, false);

        //Hide keyboard
        KeyboardUtils.hideKeyboard(getActivity());

        //New task button
        Button btnNewTask = taskView.findViewById(R.id.fragmentNewTaskButton);
        btnNewTask.setOnClickListener(v -> {
            findNavController(v).navigate(R.id.task_to_createTask);
        });

        //Load data into the spinner
        //Local Database
        CategoriesDBHelper categoriesDBHelper = new CategoriesDBHelper(getActivity().getApplicationContext());
        ArrayList<String> spinnerEntries = new ArrayList<>();
        Cursor cursor = categoriesDBHelper.getAllCategories();
        String language = Locale.getDefault().getLanguage();
        //Check language
        if (cursor.getColumnIndex(language) == -1) {
            while(cursor.moveToNext()) {
                spinnerEntries.add(cursor.getString(cursor.getColumnIndex(CategoriesContract.CategoriesEntry.CAT_NAME)));
            }
        } else {
            while(cursor.moveToNext()) {
                spinnerEntries.add(cursor.getString(cursor.getColumnIndex(language)));
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.main_spinner_item_layout, spinnerEntries);

        //Filter
        //Spinner filter
        Spinner spinnerFilter = taskView.findViewById(R.id.fragmentTaskSpinnerCategoryFilter);
        spinnerFilter.setAdapter(arrayAdapter);
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

    /**
     * Get the tasks data from database
     *
     * @param v The actual view
     * @param category The category to filter the tasks list
     * */
    private void getData(View v, String category) {
        taskAdapter = new TaskAdapter(getActivity());

        //Recycler
        RecyclerView tasksRecycler = v.findViewById(R.id.fragmentTaskRecycler);
        tasksRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasksRecycler.setAdapter(taskAdapter);

        //Start array
        tasksList = new ArrayList<>();

        //Get the data from database and send it to the adapter
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        DatabaseReference tasksRef = fbDatabase.getReference(AppString.DB_TASK_REF + uid);
        tasksRef.addValueEventListener(new ValueEventListener() {
            //Get data
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get every task and save it into the array
                for (DataSnapshot dSnap: dataSnapshot.getChildren()) {
                    Task task = dSnap.getValue(Task.class);
                    //Filter the task
                    if (task.getCategory().equals(category) || category.equals("All")) {
                        AtomicBoolean taskAlreadyAdded = new AtomicBoolean(false);
                        tasksList.forEach(task1 -> {
                            if(task.getId().equals(task1.getId())) {
                                taskAlreadyAdded.set(true);
                            }
                        });
                        if(!taskAlreadyAdded.get()) {
                            tasksList.add(task);
                        }
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
