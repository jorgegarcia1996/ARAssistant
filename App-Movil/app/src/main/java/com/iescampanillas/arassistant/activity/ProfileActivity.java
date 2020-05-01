package com.iescampanillas.arassistant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.model.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser fireUser;

    private User user;

    @BindView(R.id.profile_name)
    protected TextView name;

    @BindView(R.id.profile_email)
    protected TextView email;

    @BindView(R.id.profile_image)
    protected ImageView image;

    @BindView(R.id.notification_switch)
    protected SwitchMaterial switchNotification;

    @BindView(R.id.sign_out_layout)
    protected LinearLayout signOut;

    @BindView(R.id.del_acc_layout)
    protected LinearLayout delAcc;

    @BindView(R.id.profile_toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Menu
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Sign out
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Delete Account
        delAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        fireUser = firebaseAuth.getCurrentUser();

        // Set content
        name.setText(fireUser.getDisplayName());
        email.setText(fireUser.getEmail());
        if (fireUser.getPhotoUrl() != null) {
            Picasso.get().load(fireUser.getPhotoUrl()).into(image);
        } else {
            image.setImageDrawable(getDrawable(R.drawable.no_profile_image));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
