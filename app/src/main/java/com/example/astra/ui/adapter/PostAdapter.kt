package com.example.astra.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.astra.R
import com.example.astra.data.model.Post
import com.example.astra.ui.screen.MainActivity
import com.example.astra.ui.screen.PostDetailsActivity
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val postList: List<Post> , private val context : Context) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: CircleImageView = itemView.findViewById(R.id.image_post)
        val postTitle: TextView = itemView.findViewById(R.id.text_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.postTitle.text = post.post_title
        Glide.with(holder.postImage.context)
            .load(post.post_image)
            .placeholder(R.drawable.placeholder) // Add a placeholder image
            .into(holder.postImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PostDetailsActivity::class.java)
            intent.putExtra("post", post) // Sending the Post object
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = postList.size
}
