package com.notaday.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

object AttachmentValidator {
    private const val MAX_ATTACHMENTS = 10
    private const val MAX_SIZE_BYTES = 25L * 1024L * 1024L

    fun canAddAttachment(currentCount: Int): Boolean = currentCount < MAX_ATTACHMENTS

    fun isValidSize(context: Context, uri: Uri): Boolean {
        context.contentResolver.query(uri, null, null, null, null)?.use { c ->
            val idx = c.getColumnIndex(OpenableColumns.SIZE)
            if (idx >= 0 && c.moveToFirst()) {
                return c.getLong(idx) <= MAX_SIZE_BYTES
            }
        }
        return false
    }
}
