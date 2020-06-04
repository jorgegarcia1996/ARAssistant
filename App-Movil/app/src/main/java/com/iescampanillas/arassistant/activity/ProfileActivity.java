package com.iescampanillas.arassistant.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppCode;
import com.iescampanillas.arassistant.constant.AppString;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = "ProfileActivity";

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fbDatabase;

    //Bind elements
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

    @BindView(R.id.connect_layout)
    protected LinearLayout connect;

    @BindView(R.id.del_acc_layout)
    protected LinearLayout delAcc;

    @BindView(R.id.profile_toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Butterknife
        ButterKnife.bind(this);

        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Sign out
        signOut.setOnClickListener(v -> {
            fbAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Delete Account Alert
        delAcc.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle(R.string.dialog_delete_account_title);
            builder.setMessage(R.string.dialog_delete_account_message);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.dialog_cancel, (dialog, which) -> dialog.cancel());
            builder.setPositiveButton(R.string.dialog_delete_button, (dialog, which) -> deleteAcc());
            //Mostrar el dialog
            AlertDialog alert = builder.create();
            alert.show();
        });

        // Connect activity
        connect.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ConnectActivity.class);
            startActivity(intent);
        });

        // Firebase
        fbAuth = FirebaseAuth.getInstance();
        fireUser = fbAuth.getCurrentUser();

        // Set content
        name.setText(fireUser.getDisplayName());
        email.setText(fireUser.getEmail());
        if (fireUser.getPhotoUrl() != null) {
            Picasso.get().load(fireUser.getPhotoUrl()).into(image);
        } else {
            image.setImageDrawable(getDrawable(R.drawable.no_profile_image));
        }

    }

    /**
     * Delete account from Auth and the data in Database
     * */
    public void deleteAcc() {
        fbDatabase = FirebaseDatabase.getInstance();
        String uid = fbAuth.getCurrentUser().getUid();
        fbAuth.getCurrentUser().delete();
        fbDatabase.getReference(AppString.DB_USER_REF).child(uid).removeValue();
        Toast.makeText(getApplicationContext(), R.string.toast_account_deleted, Toast.LENGTH_LONG).show();
        setResult(AppCode.DELETE_ACCOUNT);
        fbAuth.signOut();
        finish();
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user id logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
