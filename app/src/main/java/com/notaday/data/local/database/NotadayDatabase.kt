package com.notaday.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.notaday.data.local.converters.DateTimeConverters
import com.notaday.data.local.dao.AttachmentDao
import com.notaday.data.local.dao.NoteDao
import com.notaday.data.local.entities.AttachmentEntity
import com.notaday.data.local.entities.NoteEntity

@Database(entities = [NoteEntity::class, AttachmentEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverters::class)
abstract class NotadayDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun attachmentDao(): AttachmentDao
}
