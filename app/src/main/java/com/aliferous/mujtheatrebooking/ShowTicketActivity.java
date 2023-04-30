package com.aliferous.mujtheatrebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;

public class ShowTicketActivity extends AppCompatActivity {

    TextView tvBack, tvBID, tvName, tvEmail, tvDateTime, tvReg, tvSeats;
    ImageView imQR;
    String myText, name, email, reg, date, time;


    FirebaseUser firebaseUser;
    String usertype;

    int seats=1;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(uid)) {
                        // Admin
                        usertype = "admin";
                        finish();

                    } else {
                        // Not Admin
                        usertype = "user";
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);



        Intent mIntent = getIntent();
        int BookingID = mIntent.getIntExtra("BookingID",0);

        tvBack = findViewById(R.id.tvBack);
        tvBID = findViewById(R.id.tvBID);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvDateTime = findViewById(R.id.tvDatetime);
        tvReg = findViewById(R.id.tvId);
        imQR = findViewById(R.id.imageView5);
        tvSeats = findViewById(R.id.tvSeats);

        myRef.child("Bookings").child(""+BookingID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("Name").getValue(String.class);
                    email = dataSnapshot.child("Email").getValue(String.class);
                    reg = dataSnapshot.child("Registration No").getValue(String.class);
                    date = dataSnapshot.child("Date").getValue(String.class);
                    time = dataSnapshot.child("Time").getValue(String.class);
                    if(dataSnapshot.child("Seats").exists())
                        seats = Integer.parseInt(dataSnapshot.child("Seats").getValue().toString());

                    // Display the retrieved Name and Email in the TextViews
                    tvBID.setText("Booking ID : "+BookingID);
                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvReg.setText(reg);
                    tvSeats.setText("No. of Seats : " + seats);
                    tvDateTime.setText(date +"   "+ time);
                } else {
                    Toast.makeText(ShowTicketActivity.this, "Error, Please Contact Support Team", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvBID.setText("Booking ID : "+BookingID);
        tvName.setText(name);
        tvEmail.setText(email);
        tvReg.setText(reg);
        tvDateTime.setText(date +"   "+ time);
        tvSeats.setText("No. of Seats : " + seats);

        myText = ""+BookingID;

        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            imQR.setImageBitmap(mBitmap);//Setting generated QR code to imageView
            // to hide the keyboard
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                if (Objects.equals(usertype, "admin")) {
                    intent = new Intent(ShowTicketActivity.this, AdminMainActivity.class);
                }
                else {
                    intent = new Intent(ShowTicketActivity.this, MainActivity.class);
                }
                startActivity(intent);


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (Objects.equals(usertype, "admin")) {
            intent = new Intent(ShowTicketActivity.this, AdminMainActivity.class);
        }
        else {
            intent = new Intent(ShowTicketActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }
}