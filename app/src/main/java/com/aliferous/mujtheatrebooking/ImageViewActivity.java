package com.aliferous.mujtheatrebooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.imageViewMain);

        Intent mIntent = getIntent();
        int val = mIntent.getIntExtra("val",1);

        if (val == 1)
            imageView.setImageResource(R.drawable.image1);
        else if (val == 2)
            imageView.setImageResource(R.drawable.image2);
        else if (val == 3)
            imageView.setImageResource(R.drawable.image3);
        else if (val == 4)
            imageView.setImageResource(R.drawable.image4);
        else if (val == 5)
            imageView.setImageResource(R.drawable.image5);
        else if (val == 6)
            imageView.setImageResource(R.drawable.image6);
        else if (val == 7)
            imageView.setImageResource(R.drawable.image7);

    }
}