package com.example.learneng.config

import android.content.Context
import com.example.learneng.db.DatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabaseBuilder(@ApplicationContext context: Context): DatabaseBuilder {
        return DatabaseBuilder(context)
    }
}