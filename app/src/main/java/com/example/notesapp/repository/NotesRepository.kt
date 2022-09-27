package com.example.notesapp.repository

import androidx.annotation.WorkerThread

class NotesRepository(private val notesDao: NotesDao) {

    @WorkerThread
    fun insert(notes: Notes) {
        notesDao.insert(notes)
    }

}