package com.example.myittaroostockinventorymanger.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myittaroostockinventorymanger.GlideApp
import com.example.myittaroostockinventorymanger.MyAppGlideModule
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.ui.item_name.ConfirmDialogFragment
import com.squareup.picasso.Picasso

class ImageShower {

    companion object {

        fun showImage(context: Context, path: String, view: ImageView) {

            if (path.isEmpty()) {
                GlideApp.with(context)
                    .load(R.drawable.no_img)
                    .circleCrop()
                    .into(view)
            } else {

                GlideApp.with(context)
                    .load(path)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {

                            Log.d("Glide", "onLoadFailed:${e?.logRootCauses("Glide")}")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    })
                    .circleCrop()
                    .into(view)
            }
        }

    }
}