package com.example.tinderfordogs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TinderForDogs.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_PETS = "PetProfile";
    public static final String COLUMN_PET_ID = "id";
    public static final String COLUMN_PET_NAME = "name";
    public static final String COLUMN_PROFILE_IMAGE = "profile_image";

    public static final String TABLE_IMAGES = "PetImages";
    public static final String COLUMN_IMAGE_ID = "id";
    public static final String COLUMN_PET_ID_FOREIGN_KEY = "pet_id";
    public static final String COLUMN_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PETS_TABLE = "CREATE TABLE " + TABLE_PETS + "("
                + COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PET_NAME + " TEXT,"
                + COLUMN_PROFILE_IMAGE + " TEXT" + ")";

        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PET_ID_FOREIGN_KEY + " INTEGER,"
                + COLUMN_IMAGE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_PET_ID_FOREIGN_KEY + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_PET_ID + "))";

        db.execSQL(CREATE_PETS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        onCreate(db);
    }
    public List<PetProfile> getAllPets() {
        List<PetProfile> pets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PETS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_PET_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_PET_NAME);
                int profileImageIndex = cursor.getColumnIndex(COLUMN_PROFILE_IMAGE);

                if (idIndex == -1 || nameIndex == -1 || profileImageIndex == -1) {
                    continue;
                }

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String profileImage = cursor.getString(profileImageIndex);

                PetProfile pet = new PetProfile(id, name, profileImage);
                pets.add(pet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pets;
    }

    public PetProfile getPetProfile(int petId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PETS,
                new String[]{COLUMN_PET_ID, COLUMN_PET_NAME, COLUMN_PROFILE_IMAGE},
                COLUMN_PET_ID + "=?",
                new String[]{String.valueOf(petId)},
                null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        int idIndex = cursor.getColumnIndex(COLUMN_PET_ID);
        int nameIndex = cursor.getColumnIndex(COLUMN_PET_NAME);
        int profileImageIndex = cursor.getColumnIndex(COLUMN_PROFILE_IMAGE);

        if (idIndex == -1 || nameIndex == -1 || profileImageIndex == -1) {
            return null;
        }

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        String profileImage = cursor.getString(profileImageIndex);

        cursor.close();
        db.close();

        return new PetProfile(id, name, profileImage);
    }

    public List<String> getAllImagesForPet(int petId) {
        List<String> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_IMAGES + " WHERE " + COLUMN_PET_ID_FOREIGN_KEY + " = " + petId;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int imageIndex = cursor.getColumnIndex(COLUMN_IMAGE);
                if (imageIndex != -1) {
                    String image = cursor.getString(imageIndex);
                    images.add(image);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return images;
    }

    // DatabaseHelper.java

    public void deleteImage(String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, COLUMN_IMAGE + " = ?", new String[]{imageUri});
        db.close();
    }



}
