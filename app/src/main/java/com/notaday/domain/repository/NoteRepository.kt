package com.notaday.domain.repository

import com.notaday.domain.model.Note
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NoteRepository {
    fun observeByDate(date: LocalDate): Flow<List<Note>>
    fun observeAll(): Flow<List<Note>>
    fun observeById(id: Long): Flow<Note?>
    fun search(query: String): Flow<List<Note>>
    suspend fun upsert(note: Note): Long
    suspend fun delete(note: Note)
}
