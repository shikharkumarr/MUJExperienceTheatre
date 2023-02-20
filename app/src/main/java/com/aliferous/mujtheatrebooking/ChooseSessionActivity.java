package com.aliferous.mujtheatrebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChooseSessionActivity extends AppCompatActivity {

    TextView tvTime1, tvTime2, tvBack, tvDate,tvseats1;
    ConstraintLayout seatsavailableLayout, choosetimeLayout;
    ImageView imTime1, imTime2;
    Button button;
    String Time;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    int result = 0;
    String x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session);

        tvTime1 = findViewById(R.id.tvtime1);
        tvTime2 = findViewById(R.id.tvtime2);
        imTime1 = findViewById(R.id.imtime1);
        imTime2 = findViewById(R.id.imtime2);
        tvseats1 = findViewById(R.id.tvseats1);
        tvBack = findViewById(R.id.tvBack);
        tvDate = findViewById(R.id.tvDate);
        button = findViewById(R.id.button);
        seatsavailableLayout = findViewById(R.id.seatsavailableLayout);
        choosetimeLayout = findViewById(R.id.choosetimeLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //reduce one seat
                myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        int seats = Integer.parseInt(snapshot.child(x).getValue().toString());
                        seats--;
                        myRef.child("SeatAvailable").child(x).setValue(""+seats);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                String Date = tvDate.getText().toString();
                Intent intent = new Intent(ChooseSessionActivity.this, LoginActivity.class);
                intent.putExtra("Date",Date);
                intent.putExtra("Time",Time);
                startActivity(intent);
            }
        });

        String currentDate = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(currentDate);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSessionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tvTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeAlpha1();
                Time = "11:00";
                y="A";
                seatsavailableLayout.setVisibility(View.VISIBLE);
                tvseats1.setText("Loading...");
                getSeats();
            }
        });

        tvTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeAlpha2();
                Time = "03:00";
                y="B";
                seatsavailableLayout.setVisibility(View.VISIBLE);
                tvseats1.setText("Loading...");
                getSeats();
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ChooseSessionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                choosetimeLayout.setVisibility(View.VISIBLE);
                                seatsavailableLayout.setVisibility(View.GONE);
                                setTimeAlpha0();

                                tvDate.setText(day + " / " + (month + 1) +" / "+ (year) );
                                x = ""+day +" " +(month + 1)+" "+year;
                                //compare x and currentDate
                                result = DateComparator.compareDates(x, currentDate);
//                                if (result == 0){
//                                    Toast.makeText(ChooseSessionActivity.this, "Book", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (result == 1){
//                                    Toast.makeText(ChooseSessionActivity.this, "Earlier Date", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (result == 2){
//                                    Toast.makeText(ChooseSessionActivity.this, "40 Days", Toast.LENGTH_SHORT).show();
//                                }

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }



    public void setTimeAlpha0() {
        tvTime1.setAlpha(0.35f);
        imTime1.setAlpha(0.35f);
        tvTime2.setAlpha(0.35f);
        imTime2.setAlpha(0.35f);
        y=null;
    }
    public void setTimeAlpha1() {
        tvTime1.setAlpha(1.0f);
        imTime1.setAlpha(1.0f);
        tvTime2.setAlpha(0.35f);
        imTime2.setAlpha(0.35f);
    }

    public void setTimeAlpha2() {
        tvTime2.setAlpha(1.0f);
        imTime2.setAlpha(1.0f);
        tvTime1.setAlpha(0.35f);
        imTime1.setAlpha(0.35f);
    }


    public void getSeats() {
        String z = x+" "+y;
        myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(""+z)) {
                    tvseats1.setText(snapshot.child(z).getValue().toString()+" of 15");
                }
                else {
                    myRef.child("SeatAvailable").child(z).setValue(15);
                    tvseats1.setText("15 of 15");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}