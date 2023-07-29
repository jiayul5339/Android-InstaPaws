package com.example.tinderfordogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView petRecyclerView;
    private PetAdapter petAdapter;
    private List<PetProfile> petProfiles = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Your Paws");
        dbHelper = new DatabaseHelper(this);

        petRecyclerView = findViewById(R.id.petRecyclerView);
        FloatingActionButton addPetButton = findViewById(R.id.addPetButton);

        petAdapter = new PetAdapter(petProfiles);
        petRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        petRecyclerView.setAdapter(petAdapter);

        addPetButton.setOnClickListener(v -> {
            // Navigate to AddPetActivity
            Intent intent = new Intent(MainActivity.this, AddPetActivity.class);
            startActivity(intent);
        });

        loadPetProfiles();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPetProfiles();
    }

    private void loadPetProfiles() {
        // Load the pet profiles from the database
        petProfiles.clear();
        petProfiles.addAll(dbHelper.getAllPets());
        petAdapter.notifyDataSetChanged();
    }
}
