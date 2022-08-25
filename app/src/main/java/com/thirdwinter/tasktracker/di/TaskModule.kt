package com.thirdwinter.tasktracker.di

import android.content.Context
import androidx.room.Room
import com.thirdwinter.tasktracker.database.dao.TaskDao
import com.thirdwinter.tasktracker.database.TaskTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {


    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TaskTrackerDatabase::class.java,
        "overplay-database"
    )
        .fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideTrackDao(database: TaskTrackerDatabase): TaskDao = database.taskDao()


}