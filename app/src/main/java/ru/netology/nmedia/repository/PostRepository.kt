package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getAll(callback: PostCallback<List<Post>>)
    fun likeById(post: Post, callback: PostCallback<Post>)
    fun save(post: Post, callback: PostCallback<Post>)
    fun removeById(id: Long, callback: PostCallback<Unit>)

    interface PostCallback<T> {
        fun onSuccess(value: T)
        fun onError(e: Exception)
    }
}