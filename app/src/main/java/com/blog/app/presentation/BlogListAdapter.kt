package com.blog.app.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blog.app.R
import com.blog.app.model.data.BlogsResponse

class BlogListAdapter(
    private var context: Context,
    private var blogData:  List<BlogsResponse>

): RecyclerView.Adapter<BlogListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_blog_list, parent, false)
        return MyViewHolder(view)
    }

    fun setData(blogData: List<BlogsResponse>) {
        this.blogData = blogData
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged",
        "ClickableViewAccessibility"
    )
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataInfo: BlogsResponse = blogData[position]

        val title = dataInfo.title

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BlogDetailsActivity::class.java)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return blogData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
