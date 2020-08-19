package com.putya.idn.chattingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.putya.idn.chattingapp.R
import com.squareup.picasso.Picasso

class FullViewImageActivity : AppCompatActivity() {
    private var imageViewer: ImageView? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_view_image)

        imageUrl = intent.getStringExtra("url")
        imageViewer = findViewById(R.id.iv_full_image)

        Picasso.get().load(imageUrl).into(imageViewer)
    }
}