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
    DatabaseReference myRef = database.getReference() , bookingref = FirebaseDatabase.getInstance().getReference("Users");

    FirebaseUser firebaseUser;
    String name, email, reg, Date, Time;

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
                    
                    bookingref.child(firebaseUser.getUid()).child("Bookings").child(""+BookingID).child("Id").setValue(""+BookingID);


                    Intent intent = new Intent(LoginActivity.this, ShowTicketActivity.class);
                    intent.putExtra("BookingID", BookingID);
                    intent.putExtra("Name", name);
                    intent.putExtra("Email", email);
                    intent.putExtra("Reg", reg);
                    intent.putExtra("Date",Date);
                    intent.putExtra("Time",Time);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}