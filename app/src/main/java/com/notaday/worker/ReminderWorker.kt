package com.notaday.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.notaday.MainActivity
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.notaday.R

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val noteId = inputData.getLong(KEY_NOTE_ID, 0L)
        val title = inputData.getString(KEY_TITLE).orEmpty()
        val priority = inputData.getString(KEY_PRIORITY).orEmpty()

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "Notaday Reminders", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title.ifBlank { "Todo Reminder" })
            .setContentText("Priority: $priority")
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(
                applicationContext,
                noteId.toInt(),
                Intent(applicationContext, MainActivity::class.java).apply {
                    putExtra("note_id", noteId)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ))
            .build()

        NotificationManagerCompat.from(applicationContext).notify(noteId.toInt(), notification)
        return Result.success()
    }

    companion object {
        const val CHANNEL_ID = "notaday_reminders"
        const val KEY_NOTE_ID = "note_id"
        const val KEY_TITLE = "title"
        const val KEY_PRIORITY = "priority"
    }
}
