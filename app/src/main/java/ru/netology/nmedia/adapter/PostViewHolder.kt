package ru.netology.nmedia.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private val avatarUrl = "http://10.0.2.2:9999/avatars/"
    private val attachmentUrl = "http://10.0.2.2:9999/images/"

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author

            Glide.with(avatar)
                .load(avatarUrl + post.authorAvatar)
                .circleCrop()
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .into(avatar)

            published.text = post.published

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

            content.text = post.content

            like.isChecked = post.likedByMe
            like.text = post.likes.toString()

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
            }

        }
    }
}