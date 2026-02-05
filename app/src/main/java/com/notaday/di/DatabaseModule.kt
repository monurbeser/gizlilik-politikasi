package com.notaday.di

import android.content.Context
import androidx.room.Room
import com.notaday.data.local.dao.AttachmentDao
import com.notaday.data.local.dao.NoteDao
import com.notaday.data.local.database.NotadayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NotadayDatabase =
        Room.databaseBuilder(context, NotadayDatabase::class.java, "notaday.db").build()

    @Provides
    fun provideNoteDao(db: NotadayDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideAttachmentDao(db: NotadayDatabase): AttachmentDao = db.attachmentDao()
}
