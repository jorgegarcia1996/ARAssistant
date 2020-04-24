package com.iescampanillas.arassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.BundleName;
import com.iescampanillas.arassistant.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;

    @BindView(R.id.textHome)
    protected TextView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ButterKnife implementation
        ButterKnife.bind(this);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        home.setText(fbUser.getDisplayName());

        home.setOnClickListener(v -> {
            fbAuth.signOut();
            setResult(RESULT_OK);
            finish();
        });
    }
}
