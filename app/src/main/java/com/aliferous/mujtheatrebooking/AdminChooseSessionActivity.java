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

public class AdminChooseSessionActivity extends AppCompatActivity {

    TextView tvTime1, tvTime2, tvTime3, tvTime4, tvBack, tvDate, tvseats1, noshow, tvSeats2plus, tvSeats2minus, tvSeats2;
    ConstraintLayout seatsavailableLayout, choosetimeLayout, noOfSeatsLayout;
    ImageView imTime1, imTime2, imTime3, imTime4;
    Button button;
    String Date, Time;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    int result = 0;
    String x, y , z;
    int noOfSeats = 1;
    int SeatCount = 15;
    Date today, maxDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_choose_session);

        tvTime1 = findViewById(R.id.tvtime1);
        tvTime2 = findViewById(R.id.tvtime2);
        tvTime3 = findViewById(R.id.tvtime3);
        tvTime4 = findViewById(R.id.tvtime4);
        noshow = findViewById(R.id.tvnoshow);
        imTime1 = findViewById(R.id.imtime1);
        imTime2 = findViewById(R.id.imtime2);
        imTime3 = findViewById(R.id.imtime3);
        imTime4 = findViewById(R.id.imtime4);
        tvseats1 = findViewById(R.id.tvseats1);
        tvBack = findViewById(R.id.tvBack);
        tvDate = findViewById(R.id.tvDate);
        button = findViewById(R.id.button);
        tvSeats2 = findViewById(R.id.tvseats2);
        tvSeats2plus = findViewById(R.id.tvseats2plus);
        tvSeats2minus = findViewById(R.id.tvseats2minus);

        seatsavailableLayout = findViewById(R.id.seatsavailableLayout);
        choosetimeLayout = findViewById(R.id.choosetimeLayout);
        noOfSeatsLayout = findViewById(R.id.noOfSeatsLayout);

        DisableButton();

        tvSeats2plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOfSeats < 15){
                    noOfSeats++;
                    tvSeats2.setText(""+noOfSeats);
                    checkSeatCount();
                }
            }
        });

        tvSeats2minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOfSeats > 1){
                    noOfSeats--;
                    tvSeats2.setText(""+noOfSeats);
                    checkSeatCount();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Date = tvDate.getText().toString();
                Intent intent = new Intent(AdminChooseSessionActivity.this, LoginActivity.class);
                intent.putExtra("Date", Date);
                intent.putExtra("Time", Time);
                intent.putExtra("z", z);
                intent.putExtra("noOfSeats", noOfSeats);
                startActivity(intent);
            }
        });

        String currentDate = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(currentDate);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminChooseSessionActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        tvTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeAlpha1();
                Time = "10 AM";
                y = "A";
                seatsavailableLayout.setVisibility(View.VISIBLE);
                noOfSeatsLayout.setVisibility(View.VISIBLE);
                tvseats1.setText("Loading...");
                getSeats();
            }
        });

        tvTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeAlpha2();
                Time = "12 Noon";
                y = "B";
                seatsavailableLayout.setVisibility(View.VISIBLE);
                noOfSeatsLayout.setVisibility(View.VISIBLE);
                tvseats1.setText("Loading...");
                getSeats();
            }
        });

        tvTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeAlpha3();
                Time = "2 PM";
                y = "C";
                seatsavailableLayout.setVisibility(View.VISIBLE);
                noOfSeatsLayout.setVisibility(View.VISIBLE);
                tvseats1.setText("Loading...");
                getSeats();
            }
        });

        tvTime4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeAlpha4();
                Time = "4 PM";
                y = "D";
                seatsavailableLayout.setVisibility(View.VISIBLE);
                noOfSeatsLayout.setVisibility(View.VISIBLE);
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
                        AdminChooseSessionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                if((month+1)<10 ) {
                                    if(day<10)
                                        tvDate.setText("0"+day + " / 0" + (month + 1) + " / " + (year));
                                    else
                                        tvDate.setText(day + " / 0" + (month + 1) + " / " + (year));
                                }

                                else {
                                    if(day<10)
                                        tvDate.setText("0"+day + " / " + (month + 1) + " / " + (year));
                                    else
                                        tvDate.setText(day + " / " + (month + 1) + " / " + (year));
                                }


                                Date = tvDate.getText().toString();
                                x = "" + day + " " + (month + 1) + " " + year;

                                choosetimeLayout.setVisibility(View.VISIBLE);
                                seatsavailableLayout.setVisibility(View.GONE);
                                noOfSeatsLayout.setVisibility(View.GONE);


                                Calendar todayCalendar = Calendar.getInstance();
                                int todayYear = todayCalendar.get(Calendar.YEAR);
                                int todayMonth = todayCalendar.get(Calendar.MONTH);
                                int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

                                y = "" + todayDay + " " + (todayMonth+1) + " " + todayYear;



                                // Compare the dates
                                if (x.equals(y)) {
                                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                                    int currentmins = calendar.get(Calendar.MINUTE);
                                    boolean testhr = checktimings(currentHour,10);
                                    boolean testmin = checktimings(currentmins,50);
                                    if (!testhr && testmin){
                                        tvTime1.setVisibility(View.GONE);
                                        imTime1.setVisibility(View.GONE);
                                    }

                                    testhr = checktimings(currentHour,14);
                                    testmin = checktimings(currentmins,50);
                                    if (!testhr && testmin){
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
        tvTime3.setAlpha(0.35f);
        imTime3.setAlpha(0.35f);
        tvTime4.setAlpha(0.35f);
        imTime4.setAlpha(0.35f);
        y = null;
    }

    public void setTimeAlpha1() {
        tvTime1.setAlpha(1.0f);
        imTime1.setAlpha(1.0f);
        tvTime2.setAlpha(0.35f);
        imTime2.setAlpha(0.35f);
        tvTime3.setAlpha(0.35f);
        imTime3.setAlpha(0.35f);
        tvTime4.setAlpha(0.35f);
        imTime4.setAlpha(0.35f);
    }

    public void setTimeAlpha2() {
        tvTime2.setAlpha(1.0f);
        imTime2.setAlpha(1.0f);
        tvTime1.setAlpha(0.35f);
        imTime1.setAlpha(0.35f);
        tvTime3.setAlpha(0.35f);
        imTime3.setAlpha(0.35f);
        tvTime4.setAlpha(0.35f);
        imTime4.setAlpha(0.35f);
    }

    public void setTimeAlpha3() {
        tvTime3.setAlpha(1.0f);
        imTime3.setAlpha(1.0f);
        tvTime1.setAlpha(0.35f);
        imTime1.setAlpha(0.35f);
        tvTime2.setAlpha(0.35f);
        imTime2.setAlpha(0.35f);
        tvTime4.setAlpha(0.35f);
        imTime4.setAlpha(0.35f);
    }

    public void setTimeAlpha4() {
        tvTime4.setAlpha(1.0f);
        imTime4.setAlpha(1.0f);
        tvTime1.setAlpha(0.35f);
        imTime1.setAlpha(0.35f);
        tvTime2.setAlpha(0.35f);
        imTime2.setAlpha(0.35f);
        tvTime3.setAlpha(0.35f);
        imTime3.setAlpha(0.35f);
    }



    public void getSeats() {
        z = x + " " + y;
        myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("" + z)) {
                    tvseats1.setText(snapshot.child(z).getValue().toString() + " of 15");
                    SeatCount = Integer.parseInt(snapshot.child(z).getValue().toString());
                } else {
                    myRef.child("SeatAvailable").child(z).setValue(15);
                    tvseats1.setText("15 of 15");
                    SeatCount = 15;
                }

                if (SeatCount == 0){
                    DisableButton();
                }
                else if ((SeatCount - noOfSeats)<0){
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

    public void checkSeatCount(){
        if ((SeatCount - noOfSeats)<0){
            DisableButton();
        }
        else{
            EnableButton();
        }
    }

    private boolean checktimings(int time, int endtime) {

        if(time<endtime) {
            return true;
        } else {
            return false;
        }

    }
}