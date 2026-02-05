package com.notaday.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.notaday.data.local.entities.NoteEntity
import com.notaday.data.local.entities.NoteWithAttachments
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface NoteDao {
    @Transaction
    @Query("SELECT * FROM notes WHERE date = :date ORDER BY updatedAt DESC")
    fun observeByDate(date: LocalDate): Flow<List<NoteWithAttachments>>

    @Transaction
    @Query("SELECT * FROM notes ORDER BY date DESC, updatedAt DESC")
    fun observeAll(): Flow<List<NoteWithAttachments>>

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id")
    fun observeById(id: Long): Flow<NoteWithAttachments?>

    @Transaction
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun search(query: String): Flow<List<NoteWithAttachments>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(noteEntity: NoteEntity): Long

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Update
    suspend fun update(noteEntity: NoteEntity)
}
