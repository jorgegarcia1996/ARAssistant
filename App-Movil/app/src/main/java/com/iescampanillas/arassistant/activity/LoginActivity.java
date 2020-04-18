package com.iescampanillas.arassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.BundleName;
import com.iescampanillas.arassistant.constant.NumberCode;
import com.iescampanillas.arassistant.model.User;

public class LoginActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseDatabase fbDatabase;

    //Layout elements
    private EditText email, password;
    private Button btnLogin;
    private TextView txtForgotPass, txtRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //Get elements and instances
        fbAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.loginLoginButton);
        txtRegister = findViewById(R.id.loginTextRegister);
        txtForgotPass = findViewById(R.id.loginForgottenPassword);
        email = findViewById(R.id.loginTextEmailInput);
        password = findViewById(R.id.loginTextPasswordInput);

        //For fast testing, delete before release
        email.setText("admin@gmail.com");
        password.setText("admin123");

        //Login
        btnLogin.setOnClickListener(l -> {
            String ema = email.getText().toString();
            String pass = password.getText().toString();

            if (ema.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.toast_login_bad_credentials, Toast.LENGTH_LONG).show();
                return;
            }

            fbAuth.signInWithEmailAndPassword(ema, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        //Login failed
                        Toast.makeText(getApplicationContext(), R.string.toast_generic_error, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        //Login success
                        String uid = fbAuth.getCurrentUser().getUid();
                        fbDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference userRef = fbDatabase.getReference().child("user/" + uid);

                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                Bundle loginBundle = new Bundle();
                                loginBundle.putSerializable(BundleName.USER_DATA, user);

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtras(loginBundle);
                                startActivityForResult(intent, NumberCode.LOGIN_CODE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), R.string.toast_generic_error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NumberCode.LOGIN_CODE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), R.string.toast_logout_success, Toast.LENGTH_LONG).show();
            }
        }
    }
}
