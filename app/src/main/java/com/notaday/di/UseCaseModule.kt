package com.notaday.di

import com.notaday.domain.repository.NoteRepository
import com.notaday.domain.usecase.DeleteNote
import com.notaday.domain.usecase.GetAllNotes
import com.notaday.domain.usecase.GetNoteById
import com.notaday.domain.usecase.GetNotesByDate
import com.notaday.domain.usecase.NoteUseCases
import com.notaday.domain.usecase.SearchNotes
import com.notaday.domain.usecase.UpsertNote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases = NoteUseCases(
        getNotesByDate = GetNotesByDate(repository),
        getAllNotes = GetAllNotes(repository),
        searchNotes = SearchNotes(repository),
        getNoteById = GetNoteById(repository),
        upsertNote = UpsertNote(repository),
        deleteNote = DeleteNote(repository)
    )
}
