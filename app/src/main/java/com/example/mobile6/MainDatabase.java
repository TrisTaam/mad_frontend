package com.example.mobile6;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobile6.dao.ExampleDao;
import com.example.mobile6.dao.MedicineDao;
import com.example.mobile6.model.Example;
import com.example.mobile6.model.Medicine;

@Database(
        entities = {
                Example.class,
                Medicine.class,
        },
        version = 2,
        exportSchema = false
)
public abstract class MainDatabase extends RoomDatabase {
    private static volatile MainDatabase instance = null;

    public abstract ExampleDao exampleDao();
    public abstract MedicineDao medicineDao();

    public static final String DATABASE_NAME = "nhom6.db";

    public static MainDatabase getInstance(Context context) {
        if (instance != null) return instance;
        synchronized (MainDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                                context.getApplicationContext(),
                                MainDatabase.class,
                                DATABASE_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }
}
