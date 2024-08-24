package com.example.ekacareproject.repository

import android.content.Context
import com.example.ekacareproject.database.EkaCareDatabase
import com.example.ekacareproject.database.dao.EkaCareDao
import com.example.ekacareproject.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EkaCareRepository(context : Context) {
    private val ekaCareDB = EkaCareDatabase.getInstance(context)

    val ekaCareDao = ekaCareDB.dao
    val userDetails : Flow<List<UserDetails>> = ekaCareDao.getAllUserDetailsFromDB()

    suspend fun insertUserDetailsIntoDB(userDetails: UserDetails) {
        ekaCareDao.insertUserDetailsIntoDB(userDetails)
    }

    suspend fun deleteUserDetailsFromDB(userDetails: UserDetails) {
        ekaCareDao.deleteUserDetailsFromDB(userDetails)
    }

    suspend fun fetchAllUserDetailsFromDB() {
        ekaCareDao.getAllUserDetailsFromDB()
    }
}