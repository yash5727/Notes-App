package com.example.notesapp.viewModel

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.notesapp.BaseViewModel
import com.example.notesapp.repository.Notes
import com.example.notesapp.repository.NotesAppDatabase
import com.example.notesapp.repository.NotesDao
import com.example.notesapp.repository.NotesRepository
import kotlinx.coroutines.launch

open class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val notesDao: NotesDao = NotesAppDatabase.getInstance(application, ioScope).notesDao()
    protected val repository: NotesRepository = NotesRepository(notesDao)

    fun add(notes: Notes){
        mainScope.launch {
            val job = ioScope.launch {
                repository.insert(notes)
            }
            job.join()
        }
    }
}