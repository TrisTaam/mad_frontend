package com.example.mobile6.model;

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
@Entity(tableName = "examples")
public class Example {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
}
