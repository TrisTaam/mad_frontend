package com.example.mobile6.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobile6.model.Medicine;

import java.util.List;

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM medicines")
    List<Medicine> getAllMedicines();
    
    @Query("SELECT * FROM medicines WHERE name LIKE '%' || :query || '%'")
    List<Medicine> searchMedicines(String query);
    
    @Query("SELECT * FROM medicines WHERE id = :id")
    Medicine getMedicineById(String id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicine(Medicine medicine);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicines(List<Medicine> medicines);
    
    @Update
    void updateMedicine(Medicine medicine);
    
    @Delete
    void deleteMedicine(Medicine medicine);
} 