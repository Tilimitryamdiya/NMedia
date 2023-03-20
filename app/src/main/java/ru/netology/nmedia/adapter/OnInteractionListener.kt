package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onPlay(post: Post) {}
    fun onPost(post: Post) {}
    fun onAttachment(post: Post)
    fun onAd(ad: Ad) {}
}