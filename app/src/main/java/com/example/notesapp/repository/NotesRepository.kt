package com.example.notesapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao) {

    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes().asLiveData()

    @WorkerThread
    fun insert(notes: Notes) {
        notesDao.insert(notes)
    }

    @WorkerThread
    fun update(notes: Notes) {
        notesDao.updateNotes(notes)
    }

    @WorkerThread
    fun delete(notes: Notes) {
        notesDao.deleteNotes()
    }

    @WorkerThread
    fun getNote(id: Int): Flow<Notes> {
        return notesDao.getNote(id)
    }

    @WorkerThread
    fun getAllNotes(): Flow<List<Notes>> {
        return notesDao.getAllNotes()
    }

}