package com.aliferous.mujtheatrebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    Button button1, button2;
    TextView tvBack;
    EditText etPhno,etOTP;
    ImageView ExperienceTheatre;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String codeSent, codeEntered;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(uid)) {
                        // Admin
                        Intent intent = new Intent(SignInActivity.this,AdminMainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Not Admin
                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignInActivity.this, "Error, Please contact support team", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etPhno = findViewById(R.id.etphno);
        etOTP = findViewById(R.id.etotp);
        tvBack = findViewById(R.id.tvBack);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        ExperienceTheatre = findViewById(R.id.imageView7);


        ExperienceTheatre.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(SignInActivity.this,AdminLoginActivity.class);
                startActivity(intent);
                return false;
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phno = etPhno.getText().toString();
                if (phno.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Send OTP to No
                    String contact = etPhno.getText().toString();
                    contact="+91"+contact;
                    sendVerificationCode(contact);
                }


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeEntered = etOTP.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeEntered);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    void sendVerificationCode(String phno){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //CORRECT OTP AND LOGIN
                            FirebaseUser user = task.getResult().getUser();

                            assert user != null;
                            String userid = user.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild("id")){
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        startActivity(intent);

                                    }

                                    else {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });






                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            //Next Page Animation
            etOTP.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(etPhno, "translationX", 0f, -1200f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(etOTP, "translationX", 1200f, 0f);
            final ObjectAnimator oa3 = ObjectAnimator.ofFloat(button1, "translationX", 0f, -1200f);
            final ObjectAnimator oa4 = ObjectAnimator.ofFloat(button2, "translationX", 1200f, 0f);
            oa1.setDuration(350);
            oa2.setDuration(350);
            oa3.setDuration(350);
            oa4.setDuration(350);
            oa1.setInterpolator(new AccelerateDecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa3.setInterpolator(new AccelerateDecelerateInterpolator());
            oa4.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    etPhno.setVisibility(View.GONE);
                    button1.setVisibility(View.GONE);
                }
            });
            oa1.start();
            oa2.start();
            oa3.start();
            oa4.start();
            //Next Page Animation Over

            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

}