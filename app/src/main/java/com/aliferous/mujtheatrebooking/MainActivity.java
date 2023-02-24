package com.aliferous.mujtheatrebooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Button button1, button2;
    ImageView imagev1,imagev2,imagev3,imagev4,imagev5,imagev6,imagev7;
    boolean isImageFitToScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        imagev1 = findViewById(R.id.imagev1);
        imagev2 = findViewById(R.id.imagev2);
        imagev3 = findViewById(R.id.imagev3);
        imagev4 = findViewById(R.id.imagev4);
        imagev5 = findViewById(R.id.imagev5);
        imagev6 = findViewById(R.id.imagev6);
        imagev7 = findViewById(R.id.imagev7);


        imagev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",1);
                startActivity(intent);
            }
        });
        imagev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",2);
                startActivity(intent);
            }
        });
        imagev3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",3);
                startActivity(intent);
            }
        });
        imagev4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",4);
                startActivity(intent);
            }
        });
        imagev5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",5);
                startActivity(intent);
            }
        });
        imagev6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",6);
                startActivity(intent);
            }
        });
        imagev7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                intent.putExtra("val",7);
                startActivity(intent);
            }
        });



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewTicketsActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseSessionActivity.class);
                startActivity(intent);
            }
        });
    }
}