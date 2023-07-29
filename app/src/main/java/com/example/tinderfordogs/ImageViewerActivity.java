package com.example.tinderfordogs;
// ImageViewerActivity.java

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageViewerActivity extends AppCompatActivity {
    private String imageUri;
    private DatabaseHelper dbHelper;
    private ImageView fullScreenImageView;
    private Button deleteButton;
    private int petId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fullScreenImageView = findViewById(R.id.fullScreenImageView);
        deleteButton = findViewById(R.id.deleteButton);

        Intent intent = getIntent();
        imageUri = intent.getStringExtra("imageUri");
        petId = intent.getIntExtra("petId", -1);
        dbHelper = new DatabaseHelper(this);

        Glide.with(this)
                .load(imageUri)
                .into(fullScreenImageView);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ImageViewerActivity.this)
                        .setTitle("Delete Image")
                        .setMessage("Are you sure you want to delete this image?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbHelper.deleteImage(imageUri);  // You need to implement this method in DatabaseHelper
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, PetProfileActivity.class);
                intent.putExtra("petId", petId);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

