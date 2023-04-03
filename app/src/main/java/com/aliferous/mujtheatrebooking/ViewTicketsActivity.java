package com.aliferous.mujtheatrebooking;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.Ticket_Adapter;
import Model.BookingId_Model;
import Model.Ticket_Model;

public class ViewTicketsActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    private RecyclerView recyclerView_tickets;

    Ticket_Adapter ticket_adapter;

    TextView back;
    private TextView loading;
    String load = "Loading";

    private List<Ticket_Model> tickets;

    private List<BookingId_Model> bookingIds;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        recyclerView_tickets = findViewById(R.id.ticket_recycler);
        loading = findViewById(R.id.tvLoading);


        back = findViewById(R.id.tvBack);

        tickets = new ArrayList<>();
        bookingIds = new ArrayList<>();
        recyclerView_tickets.setHasFixedSize(true);
        recyclerView_tickets.setLayoutManager(new LinearLayoutManager(ViewTicketsActivity.this));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTicketsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        read_ticket_items();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewTicketsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void read_ticket_items() {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Bookings");

        DatabaseReference booking_reference = FirebaseDatabase.getInstance().getReference("Bookings");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dsnapshot) {
                bookingIds.clear();

                for (DataSnapshot snapshot : dsnapshot.getChildren()) {
                    BookingId_Model bid = snapshot.getValue(BookingId_Model.class);


                    if (bid != null && bid.getId() != null) {
                        bookingIds.add(bid);
                    }
                }

                tickets.clear();
                for (final BookingId_Model bookings : bookingIds) {

                    booking_reference.child(bookings.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {



                            Ticket_Model user = snapshot.getValue(Ticket_Model.class);

                            assert user != null;
                            user.setId(snapshot.child("Id").getValue().toString());
                            user.setName(snapshot.child("Name").getValue().toString());
                            user.setDate(snapshot.child("Date").getValue().toString());
                            user.setTime(snapshot.child("Time").getValue().toString());


                            tickets.add(user);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ViewTicketsActivity.this, "Database Error : "+error, Toast.LENGTH_SHORT).show();

                        }
                    });

                }

                new CountDownTimer(1500, 500) {
                    public void onFinish() {



                        ticket_adapter = new Ticket_Adapter(ViewTicketsActivity.this,tickets,false);
                        recyclerView_tickets.setAdapter(ticket_adapter);

                        loading.setVisibility(GONE);
                        recyclerView_tickets.setVisibility(View.VISIBLE);
                    }

                    public void onTick(long millisUntilFinished) {

                        load=load+".";
                        loading.setText(load);


                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();

                //put delay


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tickets.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ticket_Model user = snapshot.getValue(Ticket_Model.class);


                    if (user != null && user.getName() != null) {
                        tickets.add(user);
                    }

                }

                ticket_adapter = new Ticket_Adapter(ViewTicketsActivity.this,tickets,false);
                recyclerView_tickets.setAdapter(ticket_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }
}