package com.iescampanillas.arassistant.activity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.User;
import com.iescampanillas.arassistant.utils.Generator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "RegisterActivity";

    //Bind elements
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
    @BindView(R.id.register_lastname_text)
    protected TextInputEditText nameText;

    @BindView(R.id.register_name_text)
    protected TextInputEditText lastNameText;

    @BindView(R.id.register_email_text)
    protected TextInputEditText emailText;

    @BindView(R.id.register_pass_text)
    protected TextInputEditText passText;

    @BindView(R.id.register_repeatpass_text)
    protected TextInputEditText repeatPassText;

    @BindView(R.id.register_accept_btn)
    protected Button acceptBtn;

    @BindView(R.id.register_toolbar)
    protected Toolbar toolbar;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private DatabaseReference mDatabase;

    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ButterKnife
        ButterKnife.bind(this);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Cancel Register
        toolbar.setNavigationOnClickListener(v -> finish());

        //Accept Register
        acceptBtn.setOnClickListener(v -> checkFields());

    }

    /**
     * Check the inputs
     *
     * */
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
            registerUser(name, lastName, email, pass);
        }
    }

    /**
     * Create a new user in firebase
     *
     * @param name User's name.
     * @param lastName User's last name.
     * @param email User's email.
     * @param pass User's password
     * */
    private void registerUser(String name, String lastName, String email, String pass) {
        //Create the user in firebase auth
        fbAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    UserProfileChangeRequest.Builder user1 = new UserProfileChangeRequest.Builder();
                    user1.setDisplayName(name + " " + lastName);
                    if (fbAuth.getCurrentUser() != null) {
                        fbAuth.getCurrentUser().updateProfile(user1.build())
                                .addOnCompleteListener(task1 -> {
                                    //Clear fields
                                    nameText.getText().clear();
                                    lastNameText.getText().clear();
                                    emailText.getText().clear();
                                    passText.getText().clear();
                                    repeatPassText.getText().clear();
                                    user = new User();
                                    user.setName(name);
                                    user.setSurname(lastName);
                                    user.setEmail(email);;
                                    user.setConnectID(Generator.generateId(""));
                                    //Save user's data in database
                                    if(user != null) {
                                        createUserDB(user);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.toast_generic_error),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                });
                    }
                }).addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }

    /**
     * Save the user data in Firebase Database
     *
     * @param user The user to be stored in the database
     * */
    private void createUserDB(User user) {
        mDatabase.child(AppString.DB_USER_REF).child(fbUser.getUid()).setValue(user);

        //Finish activity
        setResult(RESULT_OK);
        finish();
    }
}
