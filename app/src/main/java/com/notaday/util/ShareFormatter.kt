package com.notaday.util

import com.notaday.domain.model.Note

object ShareFormatter {
    fun format(notes: List<Note>): String {
        return notes.joinToString("\n\n") { note ->
            val status = if (note.isTodo) {
                if (note.isCompleted == true) "☑" else "☐"
            } else "•"
            "${note.date}\n$status ${note.title}\n${note.content}"
        }
    }
}
