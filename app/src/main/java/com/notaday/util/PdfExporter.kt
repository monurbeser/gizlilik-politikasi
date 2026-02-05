package com.notaday.util

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.notaday.domain.model.AttachmentType
import com.notaday.domain.model.Note
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PdfExporter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun export(
        username: String,
        notes: List<Note>,
        includePhotos: Boolean,
        dateLabel: String = LocalDate.now().toString()
    ): File {
        val doc = PdfDocument()
        val paint = Paint().apply { textSize = 12f }
        var pageNumber = 1
        var y = 60
        var page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
        val canvas = page.canvas

        canvas.drawText("$username's Notes from $dateLabel", 40f, 40f, paint)

        fun nextPage() {
            canvas.drawText("Page $pageNumber • Exported ${LocalDate.now()}", 40f, 820f, paint)
            doc.finishPage(page)
            pageNumber += 1
            page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
            y = 60
        }

        notes.forEach { note ->
            if (y > 760) nextPage()
            page.canvas.drawText("${note.date} ${if (note.isTodo) if (note.isCompleted == true) "✓" else "○" else "•"} ${note.title}", 40f, y.toFloat(), paint)
            y += 20
            note.content.chunked(80).forEach { line ->
                if (y > 780) nextPage()
                page.canvas.drawText(line, 50f, y.toFloat(), paint)
                y += 18
            }
            if (includePhotos) {
                val photos = note.attachments.count { it.fileType == AttachmentType.IMAGE }
                page.canvas.drawText("Photos attached: $photos", 50f, y.toFloat(), paint)
                y += 18
            }
            y += 8
        }

        page.canvas.drawText("Page $pageNumber • Exported ${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}", 40f, 820f, paint)
        doc.finishPage(page)
        val file = File(context.cacheDir, "notaday-export-${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use(doc::writeTo)
        doc.close()
        return file
    }
}
