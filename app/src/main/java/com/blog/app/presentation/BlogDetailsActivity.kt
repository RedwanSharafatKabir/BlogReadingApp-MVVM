package com.blog.app.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.blog.app.R
import com.blog.app.databinding.ActivityBlogDetailsBinding
import com.bumptech.glide.Glide

class BlogDetailsActivity: AppCompatActivity() {

    private lateinit var title: String
    private lateinit var description: String
    private lateinit var publishedAt: String
    private lateinit var author: String
    private lateinit var content: String
    private lateinit var imageUrl: String
    private lateinit var binding: ActivityBlogDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )

        } else {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        title = intent.getStringExtra("title").toString()
        description = intent.getStringExtra("description").toString()
        content = intent.getStringExtra("content").toString()
        publishedAt = intent.getStringExtra("publishedAt").toString()
        author = intent.getStringExtra("author").toString()
        imageUrl = intent.getStringExtra("imageUrl").toString()

        setDataOnUi()
    }

    private fun setDataOnUi(){
        binding.itemTitle.text = title
        binding.description.text = description
        binding.content.text = content
        binding.publishedAt.text = publishedAt
        binding.author.text = author
        Glide.with(this).load(imageUrl).into(binding.itemImage)
    }
}
