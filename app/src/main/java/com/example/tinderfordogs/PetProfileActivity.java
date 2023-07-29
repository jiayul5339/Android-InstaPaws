package com.example.tinderfordogs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PetProfileActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private PetProfile petProfile;
    private List<String> petImages = new ArrayList<>();
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        getSupportActionBar().setTitle("Profile");
        ImageView profileImage = findViewById(R.id.petProfileImage);
        TextView petName = findViewById(R.id.petName);
        GridView imagesGrid = findViewById(R.id.imagesGrid);
        ImageView addImageButton = findViewById(R.id.addImageButton);

        databaseHelper = new DatabaseHelper(this);
        int petId = getIntent().getIntExtra("petId", -1);

        petProfile = databaseHelper.getPetProfile(petId);
        petImages = databaseHelper.getAllImagesForPet(petId);

        Glide.with(this)
                .load(petProfile.getProfileImage())
                .centerCrop()
                .into(profileImage);

        petName.setText(petProfile.getName());

        imageAdapter = new ImageAdapter(this, petImages);
        imagesGrid.setAdapter(imageAdapter);

        // Here's the new code for starting ImageViewerActivity
        imagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PetProfileActivity.this, ImageViewerActivity.class);
                intent.putExtra("imageUri", petImages.get(position));
                intent.putExtra("petId", petProfile.getId());
                startActivity(intent);
            }
        });

        addImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddImageActivity.class);
            intent.putExtra("petId", petProfile.getId());
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        petImages.clear();
        petImages.addAll(databaseHelper.getAllImagesForPet(petProfile.getId()));
        imageAdapter.notifyDataSetChanged();
    }
}
