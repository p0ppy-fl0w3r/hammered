package com.example.hammered

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageSource")
fun imageSource(imageView: ImageView,imageUrl: String?){
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(Uri.parse("file:///android_asset/$imageUrl"))
            .into(imageView)
    }
}