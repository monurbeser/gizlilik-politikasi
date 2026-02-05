package com.notaday.presentation.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notaday.domain.model.Attachment
import com.notaday.domain.model.AttachmentType
import com.notaday.domain.model.Note
import com.notaday.domain.model.Priority
import com.notaday.domain.usecase.NoteUseCases
import com.notaday.util.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState())
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    fun load(noteId: Long) {
        if (noteId == 0L) return
        viewModelScope.launch {
            noteUseCases.getNoteById(noteId).collect { note ->
                _state.update { it.copy(note = note ?: it.note) }
            }
        }
    }

    fun updateTitle(title: String) = updateNote { copy(title = title) }
    fun updateContent(content: String) = updateNote { copy(content = content) }
    fun updateDate(date: LocalDate) = updateNote { copy(date = date) }
    fun updateIsTodo(isTodo: Boolean) = updateNote { copy(isTodo = isTodo, isCompleted = if (!isTodo) null else (isCompleted ?: false)) }
    fun updateCompleted(done: Boolean) = updateNote { copy(isCompleted = done) }
    fun updatePriority(priority: Priority?) = updateNote { copy(priority = priority) }
    fun updateDeadline(value: LocalDateTime?) = updateNote { copy(deadline = value) }
    fun updateReminder(value: LocalDateTime?) = updateNote { copy(reminderTime = value) }

    fun addAttachment(path: String, name: String, type: AttachmentType) {
        val current = _state.value.note
        val attachment = Attachment(
            noteId = current.id,
            filePath = path,
            fileName = name,
            fileType = type,
            createdAt = LocalDateTime.now()
        )
        updateNote { copy(attachments = attachments + attachment) }
    }

    fun save(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            val note = _state.value.note
            val id = noteUseCases.upsertNote(note.copy(updatedAt = LocalDateTime.now()))
            reminderScheduler.schedule(note.copy(id = id))
            onSaved()
        }
    }

    fun delete(onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            noteUseCases.deleteNote(_state.value.note)
            onDeleted()
        }
    }

    private fun updateNote(transform: Note.() -> Note) {
        _state.update { it.copy(note = it.note.transform()) }
    }
}

data class NoteDetailState(
    val note: Note = Note()
)
