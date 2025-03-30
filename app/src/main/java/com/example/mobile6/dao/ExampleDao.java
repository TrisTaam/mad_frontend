package com.example.mobile6.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.mobile6.model.Example;

import java.util.List;

@Dao
public interface ExampleDao {
    @Query("SELECT * FROM examples")
    List<Example> getAllExamples();
}
