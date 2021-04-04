package com.test.mylifegoale.utilities;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.test.mylifegoale.R;

public class CustomBinding {
    @BindingAdapter(requireAll = false, value = {"imageUrl", "placeholder"})
    public static void setImageUrl(ImageView imageView, String str, Drawable drawable) {
        if (str == null || str.isEmpty()) {
            imageView.setImageDrawable(drawable);
        } else {
            Glide.with(imageView.getContext()).load(str).apply((BaseRequestOptions<?>) ((RequestOptions) ((RequestOptions) new RequestOptions().fitCenter()).centerCrop()).error((int) R.drawable.place_holder)).into(imageView);
        }
    }

    @BindingAdapter(requireAll = false, value = {"imageRes"})
    public static void setImage(ImageView imageView, int i) {
        imageView.setImageResource(i);
    }
}
