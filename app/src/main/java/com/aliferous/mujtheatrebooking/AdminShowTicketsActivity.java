package com.aliferous.mujtheatrebooking;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Adapter.Ticket_Adapter;
import Model.BookingId_Model;
import Model.Ticket_Model;

public class AdminShowTicketsActivity extends AppCompatActivity {


    private RecyclerView recyclerView_tickets;

    Ticket_Adapter ticket_adapter;

    TextView tvDate;
    Button ShowTickets;

    private List<Ticket_Model> tickets;

    String Date, x, y, z;
    Date today, maxDate;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_tickets);


        recyclerView_tickets = findViewById(R.id.ticket_recycler);

        ShowTickets = findViewById(R.id.showtickets);

        tvDate = findViewById(R.id.admintvDate);


        tickets = new ArrayList<>();
        recyclerView_tickets.setHasFixedSize(true);
        recyclerView_tickets.setLayoutManager(new LinearLayoutManager(AdminShowTicketsActivity.this));


        String currentDate = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(currentDate);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_tickets.setVisibility(GONE);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                today = calendar.getTime();
                calendar.add(Calendar.DATE, 40);
                maxDate = calendar.getTime();


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AdminShowTicketsActivity.this,
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


                                Calendar todayCalendar = Calendar.getInstance();
                                int todayYear = todayCalendar.get(Calendar.YEAR);
                                int todayMonth = todayCalendar.get(Calendar.MONTH);
                                int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

                                y = "" + todayDay + " " + (todayMonth+1) + " " + todayYear;





                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(today.getTime());
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                datePickerDialog.show();


            }
        });

        ShowTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_tickets.setVisibility(View.VISIBLE);
                read_ticket_items(tvDate.getText().toString());
            }
        });


    }


    private void read_ticket_items(String date) {



        DatabaseReference booking_reference = FirebaseDatabase.getInstance().getReference("Bookings");


        booking_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tickets.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ticket_Model user = snapshot.getValue(Ticket_Model.class);


                    if (user != null && Objects.equals(user.getDate(), date)) {
                        tickets.add(user);
                    }

                }

                ticket_adapter = new Ticket_Adapter(AdminShowTicketsActivity.this,tickets,false);
                recyclerView_tickets.setAdapter(ticket_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}