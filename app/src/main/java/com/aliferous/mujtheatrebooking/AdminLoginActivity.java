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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class AdminLoginActivity extends AppCompatActivity {


    Button button;
    TextView tvBack;
    EditText etUsername, etPass;


    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String username,pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance();

        button = findViewById(R.id.button);
        tvBack = findViewById(R.id.tvBack);
        etUsername = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPass);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass = etPass.getText().toString();
                username = etUsername.getText().toString();

                if (pass.equals("") || username.equals("")){
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                }

                else {

                    adminLogin(username,pass);

                }

            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


    }

    private void adminLogin(String username,String pass) {

        auth.signInWithEmailAndPassword(username,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminLoginActivity.this, SignInActivity.class);
        startActivity(intent);
    }


}