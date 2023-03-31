package kaz.dev.recepiesbook.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

class RecipesRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {

            imageView.load(imageUrl) {
                crossfade(600)
            }
        }
    }
}