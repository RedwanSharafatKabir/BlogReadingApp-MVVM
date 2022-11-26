package com.overlay.pockettv

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.overlay.pockettv.databinding.ActivityLiveBinding

class LiveActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    var mediaControls: MediaController? = null
    private lateinit var binding: ActivityLiveBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        webView = binding.smartLiveWebView
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.playNow.setOnClickListener {
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            binding.loadingAnimation.visibility = View.VISIBLE

//            if (mediaControls == null) {
//                mediaControls = MediaController(this)
//                mediaControls!!.setAnchorView(this.binding.videoView)
//            }
//
//            binding.videoView.setMediaController(mediaControls)
//            binding.videoView.setVideoPath(resources.getString(R.string.serverUrl))
//            binding.videoView.requestFocus()
//            binding.videoView.start()

            webView.webViewClient = WebViewClient()
            webView.loadUrl(resources.getString(R.string.serverUrl))
            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportZoom(true)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    binding.secondLayout.visibility = View.GONE
                    binding.loadingAnimation.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@LiveActivity)
        alertDialogBuilder.setTitle("EXIT !")
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            finish()
            finishAffinity()
        }

        alertDialogBuilder.setNeutralButton(
            "No"
        ) { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
