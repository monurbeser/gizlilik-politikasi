package com.notaday.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Note(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val date: LocalDate = LocalDate.now(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isTodo: Boolean = false,
    val isCompleted: Boolean? = null,
    val deadline: LocalDateTime? = null,
    val reminderTime: LocalDateTime? = null,
    val priority: Priority? = null,
    val attachments: List<Attachment> = emptyList()
)
