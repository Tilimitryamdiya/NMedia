package ru.netology.nmedia.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.view.loadCircleCrop

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private val avatarUrl = "${BuildConfig.BASE_URL}/avatars/"
    private val attachmentUrl = "${BuildConfig.BASE_URL}/images/"

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            avatar.loadCircleCrop(avatarUrl + post.authorAvatar)
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = post.likes.toString()

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            if (post.attachment != null) {
                attachment.visibility = View.VISIBLE
                attachment.contentDescription = post.attachment.description
                Glide.with(attachment)
                    .load(attachmentUrl + post.attachment.url)
                    .timeout(10_000)
                    .into(binding.attachment)
            } else {
                attachment.visibility = View.GONE
            }
        }
    }
}