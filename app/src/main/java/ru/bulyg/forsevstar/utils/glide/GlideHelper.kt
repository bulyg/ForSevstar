package ru.bulyg.forsevstar.utils.glide

import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadImage(path: String, container: ImageView) {
    Glide.with(container.context)
        .load(path)
        .into(container)
}