package com.example.notesapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.notesapp.BaseViewModel
import com.example.notesapp.repository.Notes
import com.example.notesapp.repository.NotesAppDatabase
import com.example.notesapp.repository.NotesDao
import com.example.notesapp.repository.NotesRepository
import kotlinx.coroutines.launch

open class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val notesDao: NotesDao = NotesAppDatabase.getInstance(application, ioScope).notesDao()
    private val repository: NotesRepository = NotesRepository(notesDao)

    private var _notes: LiveData<List<Notes>> = repository.allNotes
    val notes: LiveData<List<Notes>> get() = _notes

    var _singleNote: Notes? = null
    var singleNote: LiveData<Notes> = MutableLiveData()

    fun addNote(note: Notes) {
        mainScope.launch {
            val job = ioScope.launch {
                repository.insert(note)
            }
            job.join()
        }
    }

    fun updateNote(note: Notes) {
        mainScope.launch {
            val job = ioScope.launch {
                repository.update(note)
            }
            job.join()
        }
    }

    fun getNote(id: Int) {
        mainScope.launch {
            val job = ioScope.launch {
                singleNote = repository.getNote(id).asLiveData()
                /*repository.getNote(id).collect() {
                    singleNote.value = it
                }*/
            }
            job.join()
        }
    }

}