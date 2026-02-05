package com.notaday.util

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.notaday.domain.model.Note
import com.notaday.worker.ReminderWorker
import java.time.Duration
import java.time.LocalDateTime
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun schedule(note: Note) {
        val reminder = note.reminderTime ?: return
        val delay = Duration.between(LocalDateTime.now(), reminder).toMillis().coerceAtLeast(0)
        val data = Data.Builder()
            .putLong(ReminderWorker.KEY_NOTE_ID, note.id)
            .putString(ReminderWorker.KEY_TITLE, note.title)
            .putString(ReminderWorker.KEY_PRIORITY, note.priority?.name ?: "NONE")
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "todo_reminder_${note.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
