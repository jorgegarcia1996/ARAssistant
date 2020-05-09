package com.iescampanillas.arassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.model.User;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "RegisterActivity";

    //Layout Inputs
    @BindView(R.id.edit_profile_name_input)
    protected TextInputLayout nameInput;

    @BindView(R.id.edit_profile_lastname_input)
    protected TextInputLayout lastNameInput;

    @BindView(R.id.edit_profile_email_input)
    protected TextInputLayout emailInput;

    //Layout input text
    @BindView(R.id.edit_profile_name_text)
    protected TextInputEditText nameText;

    @BindView(R.id.edit_profile_lastname_text)
    protected TextInputEditText lastNameText;

    @BindView(R.id.edit_profile_email_text)
    protected TextInputEditText emailText;

    //Buttons
    @BindView(R.id.edit_profile_accept_btn)
    protected Button acceptBtn;

    //Toolbar
    @BindView(R.id.edit_profile_toolbar)
    protected Toolbar toolbar;

    private String dbName;
    private String dbLastName;
    private String dbEmail;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private DatabaseReference mDatabase;

    private HashMap user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //ButterKnife implementation
        ButterKnife.bind(this);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user");

        //Cancel Register
        toolbar.setNavigationOnClickListener(v -> finish());

        //Accept Register
       // acceptBtn.setOnClickListener(v -> checkFields());

        //Set inputs
        mDatabase.child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = (HashMap) dataSnapshot.getValue();
                nameText.setText(user.get("name").toString());
                lastNameText.setText(user.get("surname").toString());
                emailText.setText(user.get("email").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private void checkFields() {

        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();

        if (name.equals("")) {
            nameInput.setError(getString(R.string.error_empty_fields));
        } else if (name.length() < 3) {
            nameInput.setError(getString(R.string.error_min_length));
        } else if (email.equals("")) {
            emailInput.setError(getString(R.string.error_empty_fields));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError(getString(R.string.error_invalid_email));
        } else {

            if (!user.get("name").toString().equals(name)) {

            } else if (!(user.get("surname").toString().equals(lastName))) {

            } else if (!user.get("email").toString().equals(email)) {

            } else {
                editUserProfile(name, lastName, email);
            }
        }
    }

    private void editUserProfile(@Nullable String name, @Nullable String lastName, @Nullable String email) {
        UserProfileChangeRequest.Builder user = new UserProfileChangeRequest.Builder();
        user.setDisplayName(name + " " + lastName);
    }

}
