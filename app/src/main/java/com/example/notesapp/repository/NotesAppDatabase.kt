package com.example.notesapp.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Notes::class,], version = 1, exportSchema = false)
abstract class NotesAppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    companion object {

        @Volatile
        private var INSTANCE: NotesAppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): NotesAppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesAppDatabase::class.java,
                        "notes_database"
                    )
                        .addCallback(UserRoomDatabaseCallback(scope))
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
        private class UserRoomDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }

            fun emptyDatabase(notesDao: NotesDao) {
                notesDao.deleteAll()
            }
        }
    }
}