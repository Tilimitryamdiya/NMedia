package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dialog.SignInDialog
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.MediaModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    authorId = 0L,
    likedByMe = false,
    likes = 0,
    published = "",
    attachment = null
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val appAuth: AppAuth
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val data: LiveData<FeedModel> =
        appAuth.data
            .flatMapLatest { authState ->
                repository.data.map { posts ->
                    FeedModel(
                        posts.map {
                            it.copy(ownedByMe = authState?.id == it.authorId)
                        },
                        posts.isEmpty()
                    )
                }
            }
            .asLiveData(Dispatchers.Default)
    val newerCount: LiveData<Int> = data.switchMap {
        val latestPostId = it.posts.firstOrNull()?.id ?: 0L
        repository.getNewerCount(latestPostId).asLiveData()
    }
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _media = MutableLiveData<MediaModel?>(null)
    val media: LiveData<MediaModel?>
        get() = _media

    init {
        loadPosts()
    }

    fun changePhoto(uri: Uri, file: File) {
        _media.value = MediaModel(uri, file)
    }

    fun clearPhoto() {
        _media.value = null
    }

    fun isAuthorized(manager: FragmentManager): Boolean {
        return if (appAuth.data.value != null) {
            true
        } else {
            SignInDialog().show(manager, SignInDialog.TAG)
            false
        }
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun readAll() = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            repository.showNewerPosts()
        }
        _dataState.value = FeedModelState()
    }

    fun save() {
        edited.value?.let {
            if (it !== empty) {
                viewModelScope.launch {
                    try {
                        when (val media = media.value) {
                            null -> repository.save(it)
                            else -> {
                                repository.saveWithAttachment(it, media)
                            }
                        }
                        _postCreated.value = Unit
                        edited.value = empty
                        clearPhoto()
                        _dataState.value = FeedModelState()
                    } catch (e: Exception) {
                        _dataState.value = FeedModelState(error = true)
                    }
                }
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(post: Post) {
        viewModelScope.launch {
            try {
                repository.likeById(post)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }

        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }
}