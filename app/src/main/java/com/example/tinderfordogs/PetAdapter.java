package com.example.tinderfordogs;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<PetProfile> petProfiles;

    public PetAdapter(List<PetProfile> petProfiles) {
        this.petProfiles = petProfiles;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetProfile currentPet = petProfiles.get(position);
        holder.petName.setText(currentPet.getName());

        Glide.with(holder.itemView.getContext())
                .load(currentPet.getProfileImage())
                .into(holder.petProfileImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PetProfileActivity.class);
            intent.putExtra("petId", currentPet.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return petProfiles.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView petProfileImage;
        TextView petName;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            petProfileImage = itemView.findViewById(R.id.petProfileImage);
            petName = itemView.findViewById(R.id.petName);
        }
    }
}

