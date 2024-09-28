package com.blog.app.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blog.app.R
import com.blog.app.databinding.ActivityMainBinding
import com.blog.app.model.data.Article
import com.blog.app.viewmodel.BlogsViewModel
import com.blog.app.model.handleResponse.Resource

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var blogsViewModel: BlogsViewModel
    private lateinit var articleList: MutableList<Article>
    private lateinit var blogListAdapter: BlogListAdapter
    private var recyclerViewState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        blogsViewModel = ViewModelProvider(this)[BlogsViewModel::class.java]

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

        articleList = arrayListOf()
        binding.articleList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        binding.articleList.setHasFixedSize(false)
        blogListAdapter = BlogListAdapter(this, articleList)

        binding.articleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()!!
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBlogsList() {
        try {
            articleList.clear()
            binding.progressBar.visibility = View.VISIBLE

            blogsViewModel.getBlogsList("keyword", resources.getString(R.string.apiKey))

            blogsViewModel.getBlogsResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let {
                                try {
                                    articleList.addAll(it.articles!!)
                                    blogListAdapter.setData(articleList)
                                    binding.articleList.adapter = blogListAdapter
                                    blogListAdapter.notifyDataSetChanged()
                                    binding.articleList.layoutManager!!.onRestoreInstanceState(recyclerViewState)

                                    binding.progressBar.visibility = View.GONE

                                } catch (e: Exception){
                                    Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_LONG).show()
                                    binding.progressBar.visibility = View.GONE
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                binding.progressBar.visibility = View.GONE

                                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                                alertDialogBuilder.setTitle("Error !")
                                alertDialogBuilder.setMessage(it)
                                alertDialogBuilder.setCancelable(true)

                                alertDialogBuilder.setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }

                                alertDialogBuilder.setNegativeButton("Reload") { dialog, _ ->
                                    dialog.dismiss()
                                    getBlogsList()
                                }

                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            }
                        }

                        is Resource.Loading -> {
                            // stop loader
                        }
                    }
                }
            }

        } catch (e: Exception){
            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_LONG).show()
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        getBlogsList()
        super.onResume()
    }
}
