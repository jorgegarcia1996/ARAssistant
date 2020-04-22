package com.iescampanillas.arassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.BundleName;
import com.iescampanillas.arassistant.constant.NumberCode;
import com.iescampanillas.arassistant.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "LoginActivity";

    //Firebase
    private FirebaseAuth fbAuth;

    //Text inputs
    @BindView(R.id.loginTextEmailInput)
    protected TextInputEditText email;

    @BindView(R.id.loginTextPasswordInput)
    protected TextInputEditText password;

    //Buttons
    @BindView(R.id.loginLoginButton)
    protected Button btnLogin;

    @BindView(R.id.loginForgottenPassword)
    protected MaterialTextView btnForgotPass;

    @BindView(R.id.loginTextRegister)
    protected MaterialTextView btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ButterKnife implementation
        ButterKnife.bind(this);

        //Firebase Auth
        fbAuth = FirebaseAuth.getInstance();

        //Login
        btnLogin.setOnClickListener(l -> {
            String ema = email.getText().toString();
            String pass = password.getText().toString();

            if (ema.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.toast_login_bad_credentials, Toast.LENGTH_LONG).show();
                return;
            }
            //Start login process
            fbAuth.signInWithEmailAndPassword(ema, pass).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    //Login failed
                    Toast.makeText(getApplicationContext(), R.string.toast_generic_error, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //Login success
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivityForResult(intent, NumberCode.LOGIN_CODE);
                }
            });
        });

        //Password recovery
        btnForgotPass.setOnClickListener(l -> {
            String ema = email.getText().toString();
            if (Patterns.EMAIL_ADDRESS.matcher(ema).matches()) {
                fbAuth.sendPasswordResetEmail(ema).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), R.string.toast_reset_password_email_sent, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                email.setError(getString(R.string.error_invalid_email));
            }
        });

        //Register
        btnRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(registerIntent, NumberCode.REGISTER_CODE);
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
        if(requestCode == NumberCode.REGISTER_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, getString(R.string.toast_register_succeed),
                Toast.LENGTH_SHORT).show();
            }
        }
    }
}
