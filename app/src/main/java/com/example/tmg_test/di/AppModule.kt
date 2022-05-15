package com.example.tmg_test.di

import android.content.Context
import androidx.room.Room
import com.example.tmg_test.data.Database
import com.example.tmg_test.repository.GamesRepository
import com.example.tmg_test.repository.PlayersRepository
import com.example.tmg_test.repository.ResourceRepository
import com.example.tmg_test.repository.SchedulersRepository
import com.example.tmg_test.repository.impl.ResourceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideSchedulersRepository(): SchedulersRepository {
        return SchedulersRepository(AndroidSchedulers.mainThread(), Schedulers.io())
    }
    @Provides
    @Singleton
    fun provideResourceRepository(@ApplicationContext context: Context): ResourceRepository {
        return ResourceRepositoryImpl(context)
    }


    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ) : Database {
        return Room.databaseBuilder(context, Database::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideHistoryDao(database: Database) : PlayersRepository {
        return database.playersDao()
    }

    @Singleton
    @Provides
    fun provideGamesDao(database: Database) : GamesRepository {
        return database.gamesDao()
    }
}