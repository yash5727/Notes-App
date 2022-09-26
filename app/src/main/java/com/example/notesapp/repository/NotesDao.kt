package com.example.notesapp.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    fun insert(notes: Notes)

    @Update
    fun updateNotes(vararg notes: Notes)

    @Delete
    fun deleteNotes(vararg notes: Notes)

    @Query("DELETE FROM notes")
    fun deleteAll()

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun getNote(id: Int): Flow<Notes>

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Notes>>

}