package com.notaday.domain.model

import java.time.LocalDateTime

enum class AttachmentType { IMAGE, DOCUMENT }

data class Attachment(
    val id: Long = 0,
    val noteId: Long = 0,
    val filePath: String,
    val fileType: AttachmentType,
    val fileName: String,
    val createdAt: LocalDateTime
)
