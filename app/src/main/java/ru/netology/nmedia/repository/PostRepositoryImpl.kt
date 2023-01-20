package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException

class PostRepositoryImpl : PostRepository {
    override fun getAll(callback: PostRepository.PostCallback<List<Post>>) {
        PostsApi.retrofitService.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if(posts == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }

    override fun likeById(post: Post, callback: PostRepository.PostCallback<Post>) {
        if (!post.likedByMe) {
            PostsApi.retrofitService.likeById(post.id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        val posts = response.body()
                        if(posts == null) {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        callback.onSuccess(posts)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(Exception(t))
                    }

                })
        } else {
            PostsApi.retrofitService.dislikeById(post.id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        val posts = response.body()
                        if(posts == null) {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        callback.onSuccess(posts)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(Exception(t))
                    }

                })
        }
    }

    override fun save(post: Post, callback: PostRepository.PostCallback<Post>) {
        PostsApi.retrofitService.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if(posts == null) {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })


    }

    override fun removeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        PostsApi.retrofitService.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }
}