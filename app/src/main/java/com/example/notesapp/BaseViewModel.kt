package com.example.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()

    private val mainCoroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val ioCoroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    val mainScope = CoroutineScope(mainCoroutineContext)
    val ioScope = CoroutineScope(ioCoroutineContext)
}