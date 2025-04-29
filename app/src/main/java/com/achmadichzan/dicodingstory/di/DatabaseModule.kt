package com.achmadichzan.dicodingstory.di

import androidx.room.Room
import com.achmadichzan.dicodingstory.data.local.room.StoryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<StoryDatabase> {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }

    single { get<StoryDatabase>().storyDao() }
    single { get<StoryDatabase>().remoteKeysDao() }
}