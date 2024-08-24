package com.example.ekacareproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserDetails")
data class UserDetails(
    @PrimaryKey(autoGenerate = false)
    val userId : String,
    val name : String,
    val age : String,
    val dob : String,
    val address : String
)