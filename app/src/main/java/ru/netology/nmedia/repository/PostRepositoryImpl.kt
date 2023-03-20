package ru.netology.nmedia.repository

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.*
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.enumeration.TimeSeparatorType
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.model.MediaModel
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val apiService: ApiService,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 25),
        remoteMediator = PostRemoteMediator(
            service = apiService,
            appDb = appDb,
            postDao = postDao,
            postRemoteKeyDao = postRemoteKeyDao
        ),
        pagingSourceFactory = postDao::getPagingSource,
    ).flow
        .map {
            it.map(PostEntity::toDto)
                .insertSeparators { previous, _ ->
                    if (previous?.id?.rem(5) == 0L) {
                        Ad(Random.nextLong(), "figma.jpg")
                    } else {
                        null
                    }
                }
                .insertSeparators (TerminalSeparatorType.SOURCE_COMPLETE){ previous, next ->
                    var type: TimeSeparatorType? = null
                    if (previous == null && next is Post) {
                        type = postTimeSeparator(null, next)
                    }
                    if (previous is Post && next is Post) {
                        type = postTimeSeparator(previous, next)
                    }
                    if (previous !is Post && next is Post) {
                        type = postTimeSeparator(null, next)
                    }

                    if (type != null) {
                        TimeSeparator(Random.nextLong(), type)
                    } else {
                        null
                    }
                }
        }

    private fun postTimeSeparator(prev: Post?, next: Post): TimeSeparatorType? {
        val secInDay = 24 * 60 * 60
        val currentTime = System.currentTimeMillis() / 1000

        return if (prev == null &&
            currentTime - next.published <= secInDay
        ) {
            TimeSeparatorType.TODAY
        } else if (prev == null &&
            currentTime - next.published <= secInDay * 2
        ) {
            TimeSeparatorType.YESTERDAY
        } else if (prev == null &&
            currentTime - next.published > secInDay * 2
        ) {
            TimeSeparatorType.LAST_WEEK
        } else if (prev == null) {
            null
        } else if (currentTime - prev.published <= secInDay &&
            currentTime - next.published > secInDay
        ) {
            TimeSeparatorType.YESTERDAY
        } else if (currentTime - prev.published <= secInDay * 2 &&
            currentTime - next.published > secInDay * 2
        ) {
            TimeSeparatorType.LAST_WEEK
        } else {
            null
        }
    }

//    override fun getNewerCount(latestId: Long): Flow<Int> = flow {
//        while (true) {
//            delay(10_000)
//            try {
//                val response = apiService.getNewer(latestId)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//                val body = response.body() ?: throw ApiError(response.code(), response.message())
//                postDao.insert(body.toEntity().map {
//                    it.copy(hidden = true)
//                })
//                emit(body.size)
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//        .flowOn(Dispatchers.Default)
//
//    override suspend fun showNewerPosts() {
//        postDao.readAll()
//    }

//    override suspend fun getAll() {
//        try {
//            val response = apiService.getAll()
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            postDao.insert(body.toEntity())
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }

    override suspend fun save(post: Post) {
        try {
            val response = apiService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, media: MediaModel) {
        try {
            val uploadMedia = upload(media)

            val response = apiService.save(
                post.copy(
                    attachment = Attachment(uploadMedia.id, AttachmentType.IMAGE),
                )
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    private suspend fun upload(media: MediaModel): Media {
        val part = MultipartBody.Part.createFormData(
            "file", media.file.name, media.file.asRequestBody()
        )
        val response = apiService.uploadMedia(part)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        return requireNotNull(response.body())
    }

    override suspend fun removeById(id: Long) {
        val postToDelete = postDao.getPostById(id)
        postDao.removeById(id)
        try {
            val response = apiService.removeById(id)
            if (!response.isSuccessful) {
                postDao.insert(postToDelete)
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            postDao.insert(postToDelete)
            throw NetworkError
        } catch (e: Exception) {
            postDao.insert(postToDelete)
            throw UnknownError
        }
    }

    override suspend fun likeById(post: Post) {
        try {
            val response =
                if (!post.likedByMe) {
                    apiService.likeById(post.id)
                } else {
                    apiService.dislikeById(post.id)
                }
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}