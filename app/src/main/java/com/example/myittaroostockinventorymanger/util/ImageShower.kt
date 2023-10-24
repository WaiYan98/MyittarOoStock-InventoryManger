package com.example.myittaroostockinventorymanger.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.ui.item_name.ConfirmDialogFragment

class ImageShower {

    companion object {

        fun showImage(context: Context, path: String, view: ImageView) {

            if (path.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.no_img)
                    .circleCrop()
                    .into(view)
            } else {
                Glide.with(context)
                    .load(path)
                    .circleCrop()
                    .into(view)
            }
        }

        fun showImage(dialogFragment: DialogFragment, path: String, view: ImageView) {

            if (path.isEmpty()) {
                Glide.with(dialogFragment)
                    .load(R.drawable.no_img)
                    .circleCrop()
                    .into(view)
            } else {
                Glide.with(dialogFragment)
                    .load(path)
                    .circleCrop()
                    .into(view)
            }
        }
    }
}