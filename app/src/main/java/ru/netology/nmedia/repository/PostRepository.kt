package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.MediaModel

interface PostRepository {

    val data: Flow<PagingData<Post>>
    fun getNewerCount(latestId: Long): Flow<Int>
    suspend fun showNewerPosts()
    suspend fun getAll()
    suspend fun likeById(post: Post)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, media: MediaModel)
    suspend fun removeById(id: Long)

}