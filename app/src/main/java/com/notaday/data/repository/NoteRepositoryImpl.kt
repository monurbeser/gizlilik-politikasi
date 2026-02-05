package com.notaday.data.repository

import com.notaday.data.local.dao.AttachmentDao
import com.notaday.data.local.dao.NoteDao
import com.notaday.data.local.entities.AttachmentEntity
import com.notaday.data.local.entities.NoteEntity
import com.notaday.data.local.entities.NoteWithAttachments
import com.notaday.domain.model.Attachment
import com.notaday.domain.model.AttachmentType
import com.notaday.domain.model.Note
import com.notaday.domain.model.Priority
import com.notaday.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val attachmentDao: AttachmentDao
) : NoteRepository {

    override fun observeByDate(date: LocalDate): Flow<List<Note>> =
        noteDao.observeByDate(date).map { it.map(NoteWithAttachments::toDomain) }

    override fun observeAll(): Flow<List<Note>> =
        noteDao.observeAll().map { it.map(NoteWithAttachments::toDomain) }

    override fun observeById(id: Long): Flow<Note?> =
        noteDao.observeById(id).map { it?.toDomain() }

    override fun search(query: String): Flow<List<Note>> =
        noteDao.search(query).map { it.map(NoteWithAttachments::toDomain) }

    override suspend fun upsert(note: Note): Long {
        val noteId = noteDao.upsert(note.toEntity())
        attachmentDao.deleteForNote(noteId)
        attachmentDao.insertAll(note.attachments.map {
            AttachmentEntity(
                id = if (it.noteId == noteId) it.id else 0,
                noteId = noteId,
                filePath = it.filePath,
                fileType = it.fileType.name,
                fileName = it.fileName,
                createdAt = it.createdAt
            )
        })
        return noteId
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note.toEntity())
    }
}

private fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    date = date,
    createdAt = createdAt,
    updatedAt = LocalDateTime.now(),
    isTodo = isTodo,
    isCompleted = isCompleted,
    deadline = deadline,
    reminderTime = reminderTime,
    priority = priority?.name
)

private fun NoteWithAttachments.toDomain(): Note = Note(
    id = note.id,
    title = note.title,
    content = note.content,
    date = note.date,
    createdAt = note.createdAt,
    updatedAt = note.updatedAt,
    isTodo = note.isTodo,
    isCompleted = note.isCompleted,
    deadline = note.deadline,
    reminderTime = note.reminderTime,
    priority = note.priority?.let(Priority::valueOf),
    attachments = attachments.map {
        Attachment(
            id = it.id,
            noteId = it.noteId,
            filePath = it.filePath,
            fileType = AttachmentType.valueOf(it.fileType),
            fileName = it.fileName,
            createdAt = it.createdAt
        )
    }
)
