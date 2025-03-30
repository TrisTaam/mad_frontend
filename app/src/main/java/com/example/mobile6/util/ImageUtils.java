package com.example.mobile6.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {
    public static void load(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
