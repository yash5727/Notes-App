package com.example.notesapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao) {

    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes().asLiveData()
    val allLockedNotes: LiveData<List<Notes>> = notesDao.getLockedNotes().asLiveData()
    val allUnLockedNotes: LiveData<List<Notes>> = notesDao.getUnLockedNotes().asLiveData()

    @WorkerThread
    fun insert(notes: Notes) {
        notesDao.insert(notes)
    }

    @WorkerThread
    fun update(notes: Notes) {
        notesDao.updateNotes(notes)
    }

    @WorkerThread
    fun deleteNote(id: Int) {
        notesDao.deleteNote(id)
    }

    @WorkerThread
    fun getNote(id: Int): Flow<Notes> {
        return notesDao.getNote(id)
    }
}