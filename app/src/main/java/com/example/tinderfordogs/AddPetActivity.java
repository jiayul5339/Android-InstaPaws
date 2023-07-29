package com.example.tinderfordogs;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddPetActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_PERMISSION = 200;
    private Uri imageUri;
    private ImageView imageView;
    private EditText petNameEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a Profile");
        dbHelper = new DatabaseHelper(this);

        imageView = findViewById(R.id.petImageView);
        petNameEditText = findViewById(R.id.petNameEditText);
        saveButton = findViewById(R.id.saveButton);

        imageView.setOnClickListener(v -> openGallery());
        saveButton.setOnClickListener(v -> savePetProfile());
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void savePetProfile() {
        String petName = petNameEditText.getText().toString().trim();
        String imagePath = (imageUri != null) ? imageUri.toString() : "";

        if (petName.isEmpty()) {
            Toast.makeText(this, "Please enter pet name", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PET_NAME, petName);
        values.put(DatabaseHelper.COLUMN_PROFILE_IMAGE, imagePath);

        long newRowId = db.insert(DatabaseHelper.TABLE_PETS, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error saving pet profile", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pet profile saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
