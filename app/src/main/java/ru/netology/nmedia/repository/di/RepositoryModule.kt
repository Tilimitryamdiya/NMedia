package ru.netology.nmedia.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.repository.authorization.AuthRepository
import ru.netology.nmedia.repository.authorization.AuthRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindsPostRepository(postRepository: PostRepositoryImpl): PostRepository

    @Singleton
    @Binds
    fun bindsAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository
}