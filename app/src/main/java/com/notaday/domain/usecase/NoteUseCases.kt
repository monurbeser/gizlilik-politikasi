package com.notaday.domain.usecase

import com.notaday.domain.model.Note
import com.notaday.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

data class NoteUseCases @Inject constructor(
    val getNotesByDate: GetNotesByDate,
    val getAllNotes: GetAllNotes,
    val searchNotes: SearchNotes,
    val getNoteById: GetNoteById,
    val upsertNote: UpsertNote,
    val deleteNote: DeleteNote
)

class GetNotesByDate @Inject constructor(private val repository: NoteRepository) {
    operator fun invoke(date: LocalDate): Flow<List<Note>> = repository.observeByDate(date)
}

class GetAllNotes @Inject constructor(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.observeAll()
}

class SearchNotes @Inject constructor(private val repository: NoteRepository) {
    operator fun invoke(query: String): Flow<List<Note>> = repository.search(query)
}

class GetNoteById @Inject constructor(private val repository: NoteRepository) {
    operator fun invoke(id: Long): Flow<Note?> = repository.observeById(id)
}

class UpsertNote @Inject constructor(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note): Long = repository.upsert(note)
}

class DeleteNote @Inject constructor(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.delete(note)
}
