package com.iescampanillas.arassistant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.adapter.reminder.ReminderHomeAdapter;
import com.iescampanillas.arassistant.adapter.task.TaskHomeAdapter;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Reminder;
import com.iescampanillas.arassistant.model.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";

    //Bind elements
    @BindView(R.id.navigation)
    protected NavigationView nav;

    @BindView(R.id.main_toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.home_drawer_layout)
    protected DrawerLayout drawerLayout;


    @BindView(R.id.homeTabs)
    protected TabLayout tabLayout;

    @BindView(R.id.homeRecycler)
    protected RecyclerView homeRecycler;

    private TaskHomeAdapter taskAdapter;
    private ArrayList<Task> tasksList;

    private ReminderHomeAdapter reminderAdapter;
    private ArrayList<Reminder> reminderList;

    // User data
    private ImageView userImage;
    private TextView userEmail;
    private TextView userName;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ButterKnife implementation
        ButterKnife.bind(this);

        // Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        //Bind user data elements
        userImage = nav.getHeaderView(0).findViewById(R.id.nav_image);
        userEmail = nav.getHeaderView(0).findViewById(R.id.nav_email);
        userName = nav.getHeaderView(0).findViewById(R.id.nav_username);



        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
            nav.bringToFront();
        });

        //Navigation
        nav.setCheckedItem(R.id.nav_home);
        nav.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_task:
                    drawerLayout.closeDrawers();
                    Intent taskIntent = new Intent(HomeActivity.this, TaskActivity.class);
                    startActivity(taskIntent);
                    break;
                case R.id.nav_reminder:
                    drawerLayout.closeDrawers();
                    Intent reminderIntent = new Intent(HomeActivity.this, ReminderActivity.class);
                    startActivity(reminderIntent);
                    break;
                case R.id.nav_profile:
                    drawerLayout.closeDrawers();
                    Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    break;
                case R.id.nav_logout:
                    fbAuth.signOut();
                    finish();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        });
        getTaskList();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              if(tab.getText().toString().equals(getString(R.string.label_task))) {
                  getTaskList();
              } else if(tab.getText().toString().equals(getString(R.string.label_reminder))){
                  getReminderList();
              }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getTaskList() {
        taskAdapter = new TaskHomeAdapter(this);

        //Recycler
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeRecycler.setAdapter(taskAdapter);

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
                    tasksList.add(task);
                }
                //Send the array to the adapter
                taskAdapter.setData(tasksList);
            }

            //Error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.toast_get_task_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Get the list of reminders
     */
    private void getReminderList() {
        //Adapter
        reminderAdapter = new ReminderHomeAdapter(this);

        //Recycler
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeRecycler.setAdapter(reminderAdapter);

        //Arrays
        reminderList = new ArrayList<>();

        //Firebase
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        DatabaseReference reminderRef = fbDatabase.getReference(AppString.DB_REMINDER_REF + uid);
        reminderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnap: dataSnapshot.getChildren()) {
                    Reminder reminder = dSnap.getValue(Reminder.class);
                    reminderList.add(reminder);
                }
                //Send the data to the adapter
                reminderAdapter.setData(reminderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), R.string.toast_get_reminder_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Get the user data from Firebase to user data elements
     * */
    private void setUserData() {
        if (fbUser.getPhotoUrl() != null) {
            Picasso.get().load(fbUser.getPhotoUrl()).into(userImage);
        }
        userEmail.setText(fbUser.getEmail());
        userName.setText(fbUser.getDisplayName());
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Check if user is logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            setUserData();
        }
    }
}
