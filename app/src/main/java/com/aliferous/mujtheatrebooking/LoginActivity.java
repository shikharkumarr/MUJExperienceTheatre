package com.aliferous.mujtheatrebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    Button button;
    TextView tvBack;
    EditText etName, etEmail, etReg;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser firebaseUser;
    String name, email, reg, Date, Time, z, usertype;
    int noOfSeats = 1;

    DatabaseReference myRef = database.getReference() , bookingref;

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
                        bookingref = FirebaseDatabase.getInstance().getReference("Admins");
                        finish();

                    } else {
                        // Not Admin
                        usertype = "user";
                        bookingref = FirebaseDatabase.getInstance().getReference("Users");
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        button = findViewById(R.id.button);
        tvBack = findViewById(R.id.tvBack);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etReg = findViewById(R.id.etReg);

        Intent mIntent = getIntent();
        Date = mIntent.getStringExtra("Date");
        Time = mIntent.getStringExtra("Time");
        z = mIntent.getStringExtra("z");
        noOfSeats = mIntent.getIntExtra("noOfSeats",1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = etName.getText().toString();
                email = etEmail.getText().toString();
                reg = etReg.getText().toString();

                if (name.equals("") || email.equals("") || reg.equals("")){
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                }

                else {
                  reduceSeat();
                  createOnFirebase();
                }
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChooseSessionActivity.class);
                startActivity(intent);
            }
        });


    }

    public void reduceSeat() {
        //reduce one seat
        myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int seats = Integer.parseInt(snapshot.child(z).getValue().toString());
                seats = seats - noOfSeats;
                myRef.child("SeatAvailable").child(z).setValue(seats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, ChooseSessionActivity.class);
        startActivity(intent);
    }

    public void createOnFirebase() {
        final int BookingID = new Random().nextInt(900000) + 100000;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(""+BookingID)) {
                    createOnFirebase();
                }
                else {
                    myRef = myRef.child("Bookings").child(""+BookingID);
                    myRef.child("Id").setValue(""+BookingID);
                    myRef.child("Name").setValue(name);
                    myRef.child("Email").setValue(email);
                    myRef.child("Registration No").setValue(reg);
                    myRef.child("Date").setValue(Date);
                    myRef.child("Time").setValue(Time);
                    myRef.child("Seats").setValue(noOfSeats);

                    bookingref.child(firebaseUser.getUid()).child("Bookings").child(""+BookingID).child("Id").setValue(""+BookingID);


                    //On Complete, Intent
                    Intent intent = new Intent(LoginActivity.this, ShowTicketActivity.class);
                    intent.putExtra("BookingID", BookingID);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}