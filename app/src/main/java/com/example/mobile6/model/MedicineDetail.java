package com.example.mobile6.model;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDetail extends Medicine {
    private String description;
    private String usage;
    private String direction;
    private String warning;
    private String imageUrl;
    
    @Builder(builderMethodName = "detailBuilder")
    public MedicineDetail(@NonNull String id, String name, String ingredients, 
                        String description, String usage, String direction, 
                        String warning, String imageUrl) {
        super(id, name, ingredients);
        this.description = description;
        this.usage = usage;
        this.direction = direction;
        this.warning = warning;
        this.imageUrl = imageUrl;
    }
} 