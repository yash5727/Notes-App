package com.example.notesapp.viewModel

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.BaseViewModel
import com.example.notesapp.Utils.Event
import com.example.notesapp.repository.Notes
import com.example.notesapp.repository.NotesAppDatabase
import com.example.notesapp.repository.NotesDao
import com.example.notesapp.repository.NotesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val notesDao: NotesDao = NotesAppDatabase.getInstance(application, ioScope).notesDao()
    private val repository: NotesRepository = NotesRepository(notesDao)

    private var _notes: LiveData<List<Notes>> = repository.allNotes
    val notes: LiveData<List<Notes>> get() = _notes

    fun addNote(notes: Notes) {
        mainScope.launch {
            val job = ioScope.launch {
                repository.insert(notes)
            }
            job.join()
        }
    }
/*
    fun getNotes() {
        viewModelScope.launch {
            val job = ioScope.launch {
                notes = repository.getAllNotes()
            }
            job.join()
        }
    }*/
}