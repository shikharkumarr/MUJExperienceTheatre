package com.aliferous.mujtheatrebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewTicketActivity extends AppCompatActivity {

    TextView tvBack, tvName, tvEmail, tvDateTime, tvReg;
    String name, email, reg, date, time,bId;
    Button button;
    ConstraintLayout ticketExpLayout, TicketView;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_ticket);


        Intent mIntent = getIntent();
        int BookingID = mIntent.getIntExtra("BookingID",0);
        bId = String.valueOf(BookingID);
        Toast.makeText(this, "Booking Id"+BookingID, Toast.LENGTH_SHORT).show();

        tvBack = findViewById(R.id.tvBack);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvDateTime = findViewById(R.id.tvDatetime);
        tvReg = findViewById(R.id.tvId);
        ticketExpLayout = findViewById(R.id.TicketExpiredView);
        TicketView = findViewById(R.id.constraintLayout2);

        myRef.child("Bookings").child(""+BookingID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("Name").getValue(String.class);
                    email = dataSnapshot.child("Email").getValue(String.class);
                    reg = dataSnapshot.child("Registration No").getValue(String.class);
                    date = dataSnapshot.child("Date").getValue(String.class);
                    time = dataSnapshot.child("Time").getValue(String.class);

                    if (dataSnapshot.child("Valid").exists()){
                        String valid = dataSnapshot.child("Valid").getValue(String.class);
                        if (valid.equals("No")){
                            button.setEnabled(false);
                            button.setAlpha((float) 0.35);
                            TicketView.setAlpha((float) 0.35);
                            ticketExpLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    // Display the retrieved Name and Email in the TextViews
                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvReg.setText(reg);
                    tvDateTime.setText(date +"   "+ time);
                } else {
                    Toast.makeText(AdminViewTicketActivity.this, "Error, Please Contact Support Team", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvName.setText(name);
        tvEmail.setText(email);
        tvReg.setText(reg);
        tvDateTime.setText(date +"   "+ time);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Bookings").child(""+BookingID).child("Valid").setValue("No");
                    button.setText("ADMITTED !");
            }
        });
    }
}