package com.iescampanillas.arassistant.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.iescampanillas.arassistant.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user is logged in
        if (fbAuth.getCurrentUser() == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
