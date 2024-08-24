package com.example.ekacareproject.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ekacareproject.model.UserDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface EkaCareDao {
    @Insert
    suspend fun insertUserDetailsIntoDB(userDetails: UserDetails)

    @Delete
    suspend fun deleteUserDetailsFromDB(userDetails: UserDetails)

    @Query("SELECT * from userdetails")
    fun getAllUserDetailsFromDB() : Flow<List<UserDetails>>
}