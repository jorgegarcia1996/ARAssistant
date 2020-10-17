package com.iescampanillas.arassistant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

public class ReminderActivity extends AppCompatActivity {

    //Bind elements
    @BindView(R.id.navigation)
    protected NavigationView nav;

    @BindView(R.id.main_toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.reminder_drawer_layout)
    protected DrawerLayout drawerLayout;

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
        setContentView(R.layout.activity_reminder);

        //ButterKnife
        ButterKnife.bind(this);

        // Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        //User data
        userImage = nav.getHeaderView(0).findViewById(R.id.nav_image);
        userEmail = nav.getHeaderView(0).findViewById(R.id.nav_email);
        userName = nav.getHeaderView(0).findViewById(R.id.nav_username);
        setUserData();

        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.label_reminder);
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
            nav.bringToFront();
        });

        //Navigation
        nav.setCheckedItem(R.id.nav_reminder);
        nav.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_task:
                    drawerLayout.closeDrawers();
                    Intent taskIntent = new Intent(ReminderActivity.this, TaskActivity.class);
                    startActivity(taskIntent);
                    break;
                case R.id.nav_home:
                    drawerLayout.closeDrawers();
                    Intent homeIntent = new Intent(ReminderActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_profile:
                    drawerLayout.closeDrawers();
                    Intent profileIntent = new Intent(ReminderActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    break;
                case R.id.nav_logout:
                    fbAuth.signOut();
                    finish();
                    Intent intent = new Intent(ReminderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        });

    }

    /**
     * Set the user data in the menu
     * */
    private void setUserData() {
        if (fbUser.getPhotoUrl() != null) {
            Picasso.get().load(fbUser.getPhotoUrl()).into(userImage);
        }
        userEmail.setText(fbUser.getEmail());
        userName.setText(fbUser.getDisplayName());
    }

}
