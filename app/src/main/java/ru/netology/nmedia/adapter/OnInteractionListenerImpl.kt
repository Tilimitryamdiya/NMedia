package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class OnInteractionListenerImpl(private val viewModel: PostViewModel) : OnInteractionListener {
    override fun onLike(post: Post) {
        viewModel.likeById(post.id)
    }

    override fun onShare(post: Post) {
        viewModel.shareById(post.id)
    }

    override fun onRemove(post: Post) {
        viewModel.removeById(post.id)
    }

    override fun onEdit(post: Post) {
        viewModel.edit(post)
    }


}