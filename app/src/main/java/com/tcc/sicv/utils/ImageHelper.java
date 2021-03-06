package com.tcc.sicv.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tcc.sicv.R;

public class ImageHelper {
    public static void loadImageUrl(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_car)
                .into(imageView)
        ;
    }
}
