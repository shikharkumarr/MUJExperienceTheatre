package com.aliferous.mujtheatrebooking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class AdminMainActivity extends AppCompatActivity{

    private Button scanButton,showTickets;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        scanButton = findViewById(R.id.scan_button);
        showTickets = findViewById(R.id.view_ticket_button);
        //resultTextView = findViewById(R.id.result_textview);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AdminMainActivity.this);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Scan a QR code");
                integrator.initiateScan();
            }
        });

        showTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this,AdminShowTicketsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            } else {
                //esultTextView.setText("Ticket Id"+result.getContents());
                int BookingID = Integer.parseInt(result.getContents());
                Intent intent = new Intent(AdminMainActivity.this, AdminViewTicketActivity.class);
                intent.putExtra("BookingID", BookingID);
                startActivity(intent);
            }
        }
    }
}