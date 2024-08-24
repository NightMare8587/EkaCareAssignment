package com.example.ekacareproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ekacareproject.database.dao.EkaCareDao
import com.example.ekacareproject.model.UserDetails

@Database(entities = [UserDetails::class], version = 2, exportSchema = false)
abstract class EkaCareDatabase : RoomDatabase(){
    abstract val dao : EkaCareDao

    companion object {
        @Volatile
        private var INSTANCE: EkaCareDatabase? = null

        fun getInstance(context: Context): EkaCareDatabase {
            // If the INSTANCE is not null, return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EkaCareDatabase::class.java,
                    "eka_care_database"
                )
                    // Add migration strategy, fallback to destructive migration if needed
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // Return instance
                instance
            }
        }
    }
}