package com.notaday.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val date: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isTodo: Boolean,
    val isCompleted: Boolean?,
    val deadline: LocalDateTime?,
    val reminderTime: LocalDateTime?,
    val priority: String?
)
