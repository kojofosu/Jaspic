package com.mcdev.centercrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jackandphantom.blurimage.BlurImage;

import java.io.IOException;
import java.util.BitSet;

public class CropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        ImageView cropIV = findViewById(R.id.cropIV);
        ImageView blurIV = findViewById(R.id.blurIV);
        Button saveBtn = findViewById(R.id.saveBtn);
        ImageView discardIV = findViewById(R.id.discardIV);
        ConstraintLayout imageConstraintLayout = findViewById(R.id.imageConstraintLayout);

        if (getIntent() != null) {
            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data);
                cropIV.setImageURI(data);
                BlurImage.with(this).load(bitmap).intensity(5).Async(true).into(blurIV);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //save image
        saveBtn.setOnClickListener(v -> {
            imageConstraintLayout.setDrawingCacheEnabled(true);
            Bitmap b = imageConstraintLayout.getDrawingCache();
            MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), b, null, null);
            Toast.makeText(getApplicationContext(), "Image saved!", Toast.LENGTH_SHORT).show();
            this.finish();
        });

        //cancel
        discardIV.setOnClickListener(v -> {
            this.finish();
        });
    }
}