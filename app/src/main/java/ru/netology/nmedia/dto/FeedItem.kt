package ru.netology.nmedia.dto

import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.enumeration.TimeSeparatorType

sealed interface FeedItem {
    val id: Long
}

data class Post(
    override val id: Long,
    val author: String,
    val authorAvatar: String,
    val authorId: Long,
    val content: String,
    val published: Long,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false
) : FeedItem

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class Ad(override val id: Long, val image: String) : FeedItem

data class TimeSeparator(override val id: Long, val type: TimeSeparatorType) : FeedItem