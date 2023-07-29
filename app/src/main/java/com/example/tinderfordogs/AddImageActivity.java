package com.example.tinderfordogs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AddImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    private Uri imageUri;
    private ImageView imageView;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private int petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        getSupportActionBar().setTitle("Add an Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(this);

        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);

        petId = getIntent().getIntExtra("petId", -1);
        if (petId == -1) {
            Toast.makeText(this, "Invalid pet id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        imageView.setOnClickListener(v -> openGallery());
        saveButton.setOnClickListener(v -> saveImage());
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri originalUri = data.getData();

            // Copy the file to internal storage
            String newUriString = copyFileToInternalStorage(originalUri, "image_" + System.currentTimeMillis() + ".jpg");

            if (newUriString != null) {
                imageUri = Uri.parse(newUriString);
                imageView.setImageURI(imageUri);
            } else {
                // Log error or show error message
            }
        }
    }

    private String copyFileToInternalStorage(Uri uri, String newFileName) {
        Uri returnUri = null;

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            FileOutputStream outStream = openFileOutput(newFileName, Context.MODE_PRIVATE);
            outStream.write(buffer);

            returnUri = Uri.fromFile(new File(getFilesDir(), newFileName));

            inputStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnUri != null ? returnUri.toString() : null;
    }



    private void saveImage() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IMAGE, imageUri.toString());
        values.put(DatabaseHelper.COLUMN_PET_ID_FOREIGN_KEY, petId);

        long newRowId = dbHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_IMAGES, null, values);

        if ( newRowId == -1 ) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
            finish();
        }
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
