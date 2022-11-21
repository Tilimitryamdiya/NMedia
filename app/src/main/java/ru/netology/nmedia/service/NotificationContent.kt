package ru.netology.nmedia.service

abstract class NotificationsContent

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
): NotificationsContent()

data class NewPost(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val content: String
): NotificationsContent()