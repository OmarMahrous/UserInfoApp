package com.madarsoft.userinfoapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.madarsoft.userinfoapp.data.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(database: UserDatabase) = database.userDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope