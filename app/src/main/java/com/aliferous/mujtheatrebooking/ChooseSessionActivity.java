package com.aliferous.mujtheatrebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

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

    TextView tvTime1, tvTime2, tvBack, tvDate,tvseats1,tvseatsheading;
    ImageView imTime1, imTime2, imseats;
    Button button;
    String Time;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session);

        tvTime1 = findViewById(R.id.tvtime1);
        tvTime2 = findViewById(R.id.tvtime2);
        imTime1 = findViewById(R.id.imtime1);
        imTime2 = findViewById(R.id.imtime2);
        tvBack = findViewById(R.id.tvBack);
        tvDate = findViewById(R.id.tvDate);
        button = findViewById(R.id.button);
        imseats = findViewById(R.id.imseats);
        tvseats1 = findViewById(R.id.tvseats1);
        tvseatsheading = findViewById(R.id.textView12);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Date = tvDate.getText().toString();
                Intent intent = new Intent(ChooseSessionActivity.this, LoginActivity.class);
                intent.putExtra("Date",Date);
                intent.putExtra("Time",Time);
                startActivity(intent);
            }
        });

        String date = new SimpleDateFormat("dd / MM", Locale.getDefault()).format(new Date());
        tvDate.setText(date);

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
                tvTime1.setAlpha(1.0f);
                imTime1.setAlpha(1.0f);
                tvTime2.setAlpha(0.35f);
                imTime2.setAlpha(0.35f);
                Time = "11:00";
            }
        });

        tvTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTime2.setAlpha(1.0f);
                imTime2.setAlpha(1.0f);
                tvTime1.setAlpha(0.35f);
                imTime1.setAlpha(0.35f);
                Time = "15:00";
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
                                tvDate.setText(day + " / " + (month + 1) );
                                String x = ""+day +"" +(month + 1)+"";
                                tvseatsheading.setVisibility(View.VISIBLE);
                                tvseats1.setVisibility(View.VISIBLE);
                                imseats.setVisibility(View.VISIBLE);
                                myRef.child("SeatAvailable").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.hasChild(""+x)) {
                                            tvseats1.setText(snapshot.child(x).getValue().toString()+" of 15");
                                        }
                                        else {
                                            myRef = myRef.child("SeatAvailable");
                                            myRef.child(x).setValue(15);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }
}