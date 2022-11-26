package com.overlay.pockettv

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.overlay.pockettv.databinding.ActivityHomeBinding
import android.net.Uri.*
import android.view.Window
import android.view.WindowManager

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        if (mediaControls == null) {
            mediaControls = MediaController(this)
            mediaControls!!.setAnchorView(this.binding.welcomeVideoView)
        }

        binding.welcomeVideoView.setMediaController(mediaControls)
        binding.welcomeVideoView.setVideoURI(parse("android.resource://" + packageName + "/" + R.raw.video))
        binding.welcomeVideoView.requestFocus()
        binding.welcomeVideoView.start()

        binding.welcomeVideoView.setOnCompletionListener {
            val intent = Intent(this@HomePage, LiveActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
