package com.iescampanillas.arassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.NumberCode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "RegisterActivity";

    //Layout Inputs
    @BindView(R.id.register_name_input)
    protected TextInputLayout nameInput;

    @BindView(R.id.register_lastname_input)
    protected TextInputLayout lastNameInput;

    @BindView(R.id.register_email_input)
    protected TextInputLayout emailInput;

    @BindView(R.id.register_pass_input)
    protected TextInputLayout passInput;

    @BindView(R.id.register_repeatpass_input)
    protected TextInputLayout repeatPassInput;

    //Layout input text
    @BindView(R.id.register_name_text)
    protected TextInputEditText nameText;

    @BindView(R.id.register_lastname_text)
    protected TextInputEditText lastNameText;

    @BindView(R.id.register_email_text)
    protected TextInputEditText emailText;

    @BindView(R.id.register_pass_text)
    protected TextInputEditText passText;

    @BindView(R.id.register_repeatpass_text)
    protected TextInputEditText repeatPassText;

    //Buttons
    @BindView(R.id.register_accept_btn)
    protected Button acceptBtn;

    //Toolbar
    @BindView(R.id.register_toolbar)
    protected Toolbar toolbar;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ButterKnife implementation
        ButterKnife.bind(this);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();

        //Cancel Register
        toolbar.setNavigationOnClickListener(v -> finish());

        //Accept Register
        acceptBtn.setOnClickListener(v -> checkFields());

    }

    private void checkFields() {

        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();
        String repeatPass = repeatPassText.getText().toString();

        if (name.equals("")) {
            nameInput.setError(getString(R.string.error_empty_fields));
        } else if (name.length() < 3) {
            nameInput.setError(getString(R.string.error_min_length));
        } else if (email.equals("")) {
            emailInput.setError(getString(R.string.error_empty_fields));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError(getString(R.string.error_invalid_email));
        } else if (pass.equals("")) {
            passInput.setError(getString(R.string.error_empty_fields));
        } else if (pass.length() < 6) {
            passInput.setError(getString(R.string.error_pass_length));
        } else if (!repeatPass.equals(pass)) {
            repeatPassInput.setError(getString(R.string.error_pass_match));
        } else {
            registerUser(name + " " + lastName, email, pass);
        }
    }

    private void registerUser(String name, String email, String pass) {

        fbAuth.createUserWithEmailAndPassword(email, pass)
              .addOnCompleteListener(task -> {
                  UserProfileChangeRequest.Builder user = new UserProfileChangeRequest.Builder();
                  user.setDisplayName(name);

                  if (fbAuth.getCurrentUser() != null) {
                      fbAuth.getCurrentUser().updateProfile(user.build())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              //Clear fields
                              nameText.getText().clear();
                              lastNameText.getText().clear();
                              emailText.getText().clear();
                              passText.getText().clear();
                              repeatPassText.getText().clear();

                              //Finish activity
                              setResult(RESULT_OK);
                              finish();
                          }
                      });
                  }
              }).addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }
}
