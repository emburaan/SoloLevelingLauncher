package com.sumit.todo_list.di

import android.content.Context
import com.sumit.todo_list.data.local.TaskDao
import com.sumit.todo_list.data.local.TaskDatabase
import com.sumit.todo_list.data.repository.TaskRepository
import com.sumit.todo_list.data.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodoBindingsModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}

@Module
@InstallIn(SingletonComponent::class)
object TodoProvidersModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        TaskDatabase.getInstance(context)

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()
}
