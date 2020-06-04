package com.iescampanillas.arassistant.activity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectActivity extends AppCompatActivity {

    private final static String TAG = "GenerateQRCode";

    //Bind elements
    @BindView(R.id.qrcode)
    protected ImageView qrImage;

    @BindView(R.id.connect_toolbar)
    protected Toolbar toolbar;

    private String inputValue = "";

    //QR Code
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

    //Firebase
    private FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        //Butterknife
        ButterKnife.bind(this);

        //Firebase
        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        DatabaseReference userRef = fbDatabase.getReference(AppString.DB_USER_REF)
            .child(fbUser.getUid())
            .child(AppString.DB_CONNECT_ID_REF);

        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Generate QR
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inputValue = dataSnapshot.getValue(String.class);
                generateQR();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * QR code generator
     *
     * */
    private void generateQR() {
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = Math.min(width, height);
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        }
    }
}
