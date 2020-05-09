package com.iescampanillas.arassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.model.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser fireUser;
    private FirebaseDatabase fbDatabase;
    private DatabaseReference userRef;

    private User user;

    @BindView(R.id.profile_name)
    protected TextView name;

    @BindView(R.id.profile_email)
    protected TextView email;

    @BindView(R.id.profile_image)
    protected ImageView image;

    @BindView(R.id.profile_edit_btn)
    protected ImageView editBtn;

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

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Back
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Sign out
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignOut();
            }
        });

        // Delete Account
        delAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder =
                        new MaterialAlertDialogBuilder(ProfileActivity.this);

                dialogBuilder.setTitle(getString(R.string.delete_acc_title));
                dialogBuilder.setMessage(getString(R.string.delete_acc_msg));
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                });
                dialogBuilder.setNegativeButton(getString(R.string.label_close_dialog), null);
                dialogBuilder.show();
            }
        });

        // Edit profile
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });

        // Firebase
        fbDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        fireUser = firebaseAuth.getCurrentUser();
        userRef = fbDatabase.getReference("user");

        // Set content
        name.setText(fireUser.getDisplayName());
        email.setText(fireUser.getEmail());
        if (fireUser.getPhotoUrl() != null) {
            Picasso.get().load(fireUser.getPhotoUrl()).into(image);
        } else {
            image.setImageDrawable(getDrawable(R.drawable.no_profile_image));
        }

    }

    private void deleteAccount() {
        if (fireUser != null) {
            userRef.child(fireUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                setSignOut();
                                Toast.makeText(ProfileActivity.this,
                                        getString(R.string.delete_acc_success),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this,
                                        getString(R.string.toast_generic_error),
                                        Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.getMessage());
                            }
                        });

                    } else {
                        Log.e(TAG, databaseError.getMessage());
                        Toast.makeText(ProfileActivity.this,
                                getString(R.string.toast_generic_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void setSignOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
