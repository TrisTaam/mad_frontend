package com.example.mobile6.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "medicines")
public class Medicine {
    @NonNull
    @PrimaryKey
    private String id;
    
    private String name;
    private String ingredients;
} 