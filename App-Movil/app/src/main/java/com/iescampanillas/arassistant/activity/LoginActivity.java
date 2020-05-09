package com.iescampanillas.arassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppCode;
import com.iescampanillas.arassistant.constant.AppString;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "LoginActivity";

    //SharedPreferences
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;

    //Firebase
    private FirebaseAuth fbAuth;

    //Text inputs
    @BindView(R.id.loginTextEmailInput)
    protected TextInputEditText email;

    @BindView(R.id.loginTextPasswordInput)
    protected TextInputEditText password;

    //Remember me checkbox
    @BindView(R.id.loginRememberMeCheckbox)
    protected CheckBox chkRememberMe;

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

        //Initialize shared preferences
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        //Read the login data if saved
        saveLogin = loginPreferences.getBoolean(AppString.SAVE_LOGIN_PREF, false);
        if(saveLogin) {
            email.setText(loginPreferences.getString(AppString.EMAIL_PREF, ""));
            password.setText(loginPreferences.getString(AppString.PASSWORD_PREF, ""));
            chkRememberMe.setChecked(true);
        }

        //Firebase Auth
        fbAuth = FirebaseAuth.getInstance();

        //Login
        btnLogin.setOnClickListener(l -> {
            //Set the IMM
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(email.getWindowToken(), 0);

            String ema = email.getText().toString();
            String pass = password.getText().toString();

            if (ema.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.toast_login_bad_credentials, Toast.LENGTH_LONG).show();
                return;
            }

            if(chkRememberMe.isChecked()) {
                loginPrefsEditor.putBoolean(AppString.SAVE_LOGIN_PREF, true);
                loginPrefsEditor.putString(AppString.EMAIL_PREF, ema);
                loginPrefsEditor.putString(AppString.PASSWORD_PREF, pass);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
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
                    startActivityForResult(intent, AppCode.LOGIN_CODE);
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
            startActivityForResult(registerIntent, AppCode.REGISTER_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppCode.LOGIN_CODE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), R.string.toast_logout_success, Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == AppCode.REGISTER_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, getString(R.string.toast_register_succeed),
                Toast.LENGTH_SHORT).show();
            }
        }
    }
}
