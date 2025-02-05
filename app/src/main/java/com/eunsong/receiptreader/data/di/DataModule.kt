package com.eunsong.receiptreader.data.di

import com.eunsong.receiptreader.data.TodoRepository
import com.eunsong.receiptreader.data.source.DefaultTodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    internal abstract fun bindTodoRepository(
        repository: DefaultTodoRepository,
    ): TodoRepository
}
