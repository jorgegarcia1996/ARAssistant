package com.iescampanillas.arassistant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;

public class TaskActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    protected NavigationView nav;

    @BindView(R.id.main_toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.task_drawer_layout)
    protected DrawerLayout drawerLayout;

    // User data
    protected ImageView userImage;
    protected TextView userEmail;
    protected TextView userName;

    private  FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //ButterKnife implementation
        ButterKnife.bind(this);

        // Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        userImage = nav.getHeaderView(0).findViewById(R.id.nav_image);
        userEmail = nav.getHeaderView(0).findViewById(R.id.nav_email);
        userName = nav.getHeaderView(0).findViewById(R.id.nav_username);

        setUserData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.label_task);

        //Open nav
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
                nav.bringToFront();
            }
        });

        nav.setCheckedItem(R.id.nav_task);
        nav.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    drawerLayout.closeDrawers();
                    Intent homeIntent = new Intent(TaskActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_reminder:
                    drawerLayout.closeDrawers();
                    Intent reminderIntent = new Intent(TaskActivity.this, ReminderActivity.class);
                    startActivity(reminderIntent);
                    break;
                case R.id.nav_profile:
                    drawerLayout.closeDrawers();
                    Intent profileIntent = new Intent(TaskActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    break;
                case R.id.nav_logout:
                    fbAuth.signOut();
                    finish();
                    Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        });

    }

    private void setUserData() {
        if (fbUser.getPhotoUrl() != null) {
            Picasso.get().load(fbUser.getPhotoUrl()).into(userImage);
        }
        userEmail.setText(fbUser.getEmail());
        userName.setText(fbUser.getDisplayName());
    }

}
