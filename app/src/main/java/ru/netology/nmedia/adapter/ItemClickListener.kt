package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface ItemClickListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
}