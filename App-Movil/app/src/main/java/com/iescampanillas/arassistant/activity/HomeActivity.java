package com.iescampanillas.arassistant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iescampanillas.arassistant.R;
import com.squareup.picasso.Picasso;

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

    @BindView(R.id.home_task_list_button)
    protected Button btnTaskList;

    @BindView(R.id.home_reminder_list_button)
    protected Button btnReminderList;

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

        //Buttons
        btnTaskList.setOnClickListener(v -> {
            Intent taskIntent = new Intent(HomeActivity.this, TaskActivity.class);
            startActivity(taskIntent);
        });

        btnReminderList.setOnClickListener(v -> {
            Intent reminderIntent = new Intent(HomeActivity.this, ReminderActivity.class);
            startActivity(reminderIntent);
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
