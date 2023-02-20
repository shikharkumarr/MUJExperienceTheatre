package com.aliferous.mujtheatrebooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowTicketActivity extends AppCompatActivity {

    TextView tvBack, tvBID, tvName, tvEmail, tvDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);

        Intent mIntent = getIntent();
        int BookingID = mIntent.getIntExtra("BookingID",0);
        String name = mIntent.getStringExtra("Name");
        String email = mIntent.getStringExtra("Email");
        String reg = mIntent.getStringExtra("Reg");
        String Date = mIntent.getStringExtra("Date");
        String Time = mIntent.getStringExtra("Time");


        tvBack = findViewById(R.id.tvBack);
        tvBID = findViewById(R.id.tvBID);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvDateTime = findViewById(R.id.tvDatetime);


        tvBID.setText("Booking ID : "+BookingID);
        tvName.setText(name);
        tvEmail.setText(email);
        tvDateTime.setText(Date +"   "+ Time);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTicketActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}