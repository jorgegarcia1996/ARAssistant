package com.iescampanillas.arassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.BundleName;
import com.iescampanillas.arassistant.model.User;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private TextView home;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fbAuth = FirebaseAuth.getInstance();
        home = findViewById(R.id.textHome);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable(BundleName.USER_DATA);

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        home.setText(user.toString());

        home.setOnClickListener(v -> {
            fbAuth.signOut();
            setResult(RESULT_OK);
            finish();
        });
    }
}
