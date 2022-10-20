package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Format
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val itemClickListener: ItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            like.setOnClickListener {
                itemClickListener.onLike(post)
            }
            likeCount.text = Format.getFormattedNumber(post.likes)
            share.setOnClickListener {
                itemClickListener.onShare(post)
            }
            shareCount.text = Format.getFormattedNumber(post.shared)
        }
    }
}