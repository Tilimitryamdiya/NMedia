package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val authorId: Long,
    val content: String,
    val published: String,
    val ownedByMe: Boolean,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val hidden: Boolean = false,
    @Embedded
    val attachment: Attachment?
) {
    fun toDto() =
        Post(
            id = id,
            author = author,
            authorAvatar = authorAvatar,
            authorId = authorId,
            content = content,
            published = published.toLong(),
            ownedByMe = ownedByMe,
            likes = likes,
            likedByMe = likedByMe,
            attachment = attachment
        )

    companion object {
        fun fromDto(post: Post) = PostEntity(
            id = post.id,
            author = post.author,
            authorAvatar = post.authorAvatar,
            authorId = post.authorId,
            content = post.content,
            published = post.published.toString(),
            ownedByMe = post.ownedByMe,
            likes = post.likes,
            likedByMe = post.likedByMe,
            attachment = post.attachment
        )
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
