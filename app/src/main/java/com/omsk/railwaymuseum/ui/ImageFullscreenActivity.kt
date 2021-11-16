package com.omsk.railwaymuseum.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.*
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.bumptech.glide.Glide
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.util.IMAGE_FULLSCREEN_ACTIVITY_TAG

//Использована активность вместо фрагмента для возможности поворота изображения в альбомный режим
class ImageFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_fullscreen)

        hideSystemUI()

        val imageUri = intent.extras!!.getString(IMAGE_FULLSCREEN_ACTIVITY_TAG)
        val imageView = findViewById<ImageView>(R.id.fullscreen_image)
        Glide.with(imageView.context)
                .load(imageUri)
                .into(imageView)

        imageView.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(systemBars())
            controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}