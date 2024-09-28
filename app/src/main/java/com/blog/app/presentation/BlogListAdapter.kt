package com.blog.app.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blog.app.R
import com.blog.app.model.data.Article
import com.bumptech.glide.Glide

class BlogListAdapter(
    private var context: Context,
    private var blogData:  List<Article>

): RecyclerView.Adapter<BlogListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_blog_list, parent, false)
        return MyViewHolder(view)
    }

    fun setData(blogData: List<Article>) {
        this.blogData = blogData
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged",
        "ClickableViewAccessibility"
    )
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataInfo: Article = blogData[position]

        val itemTitle = dataInfo.title
        val imageUrl = dataInfo.urlToImage
        val publishedAt = dataInfo.publishedAt
        val content = dataInfo.content
        val description = dataInfo.description
        val author = dataInfo.author

        holder.itemTitle.text = itemTitle
        holder.itemDate.text = publishedAt
        Glide.with(context).load(imageUrl).into(holder.itemImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BlogDetailsActivity::class.java)
            intent.putExtra("title", itemTitle.toString())
            intent.putExtra("description", description.toString())
            intent.putExtra("content", content.toString())
            intent.putExtra("imageUrl", imageUrl.toString())
            intent.putExtra("publishedAt", publishedAt.toString())
            intent.putExtra("author", author.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return blogData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemDate: TextView = itemView.findViewById(R.id.itemDate)
    }
}
