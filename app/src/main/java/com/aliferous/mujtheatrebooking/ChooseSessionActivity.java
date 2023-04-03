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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChooseSessionActivity extends AppCompatActivity {

    TextView tvTime1, tvTime2, tvBack, tvDate, tvseats1, noshow;
    ConstraintLayout seatsavailableLayout, choosetimeLayout;
    ImageView imTime1, imTime2;
    Button button;
    String Date, Time;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    int result = 0;
    String x, y , z;
    Date today, maxDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session);

        tvTime1 = findViewById(R.id.tvtime1);
        tvTime2 = findViewById(R.id.tvtime2);
        noshow = findViewById(R.id.tvnoshow);
        imTime1 = findViewById(R.id.imtime1);
        imTime2 = findViewById(R.id.imtime2);
        tvseats1 = findViewById(R.id.tvseats1);
        tvBack = findViewById(R.id.tvBack);
        tvDate = findViewById(R.id.tvDate);
        button = findViewById(R.id.button);
        seatsavailableLayout = findViewById(R.id.seatsavailableLayout);
        choosetimeLayout = findViewById(R.id.choosetimeLayout);

        DisableButton();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //reduce one seat
                myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        int seats = Integer.parseInt(snapshot.child(z).getValue().toString());
                        seats--;
                        myRef.child("SeatAvailable").child(z).setValue("" + seats);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                String Date = tvDate.getText().toString();
                Intent intent = new Intent(ChooseSessionActivity.this, LoginActivity.class);
                intent.putExtra("Date", Date);
                intent.putExtra("Time", Time);
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
                    y = "A";
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
                y = "B";
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
                today = calendar.getTime();
                calendar.add(Calendar.DATE, 40);
                maxDate = calendar.getTime();


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ChooseSessionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                tvDate.setText(day + " / " + (month + 1) + " / " + (year));
                                Date = tvDate.getText().toString();
                                x = "" + day + " " + (month + 1) + " " + year;

                                choosetimeLayout.setVisibility(View.VISIBLE);
                                seatsavailableLayout.setVisibility(View.GONE);


                                Calendar todayCalendar = Calendar.getInstance();
                                int todayYear = todayCalendar.get(Calendar.YEAR);
                                int todayMonth = todayCalendar.get(Calendar.MONTH);
                                int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

                                y = "" + todayDay + " " + (todayMonth+1) + " " + todayYear;



// Compare the dates
                                if (x.equals(y)) {
                                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                                    boolean test = checktimings(currentHour,10);
                                    if (!test){
                                        Toast.makeText(ChooseSessionActivity.this, "1st time", Toast.LENGTH_SHORT).show();
                                        tvTime1.setVisibility(View.GONE);
                                        imTime1.setVisibility(View.GONE);
                                    }

                                    test = checktimings(currentHour,14);
                                    if (!test){
                                        tvTime2.setVisibility(View.GONE);
                                        imTime2.setVisibility(View.GONE);
                                        noshow.setVisibility(View.VISIBLE);

                                    }
                                }
                                else{
                                    tvTime1.setVisibility(View.VISIBLE);
                                    imTime1.setVisibility(View.VISIBLE);
                                    tvTime2.setVisibility(View.VISIBLE);
                                    imTime2.setVisibility(View.VISIBLE);
                                    noshow.setVisibility(View.GONE);
                                }

                                setTimeAlpha0();
                                DisableButton();



                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(today.getTime());
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                datePickerDialog.show();


            }
        });
    }

    public void DisableButton() {
        button.setAlpha(0.4f);
        button.setEnabled(false);
    }
    public void EnableButton() {
        button.setAlpha(1f);
        button.setEnabled(true);
    }

    public void setTimeAlpha0() {
        tvTime1.setAlpha(0.35f);
        imTime1.setAlpha(0.35f);
        tvTime2.setAlpha(0.35f);
        imTime2.setAlpha(0.35f);
        y = null;
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
        z = x + " " + y;
        myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String noOfSeats;
                if (snapshot.hasChild("" + z)) {
                    tvseats1.setText(snapshot.child(z).getValue().toString() + " of 15");
                    noOfSeats = snapshot.child(z).getValue().toString();
                } else {
                    myRef.child("SeatAvailable").child(z).setValue(15);
                    tvseats1.setText("15 of 15");
                    noOfSeats = "15";
                }

                if (noOfSeats.equals("0")){
                    DisableButton();
                }
                else{
                    EnableButton();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checktimings(int time, int endtime) {


        if(time<endtime) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChooseSessionActivity.this, MainActivity.class);
        startActivity(intent);
    }
}