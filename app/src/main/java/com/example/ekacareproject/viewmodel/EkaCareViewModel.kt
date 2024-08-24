package com.example.ekacareproject.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ekacareproject.model.UserDetails
import com.example.ekacareproject.repository.EkaCareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EkaCareViewModel @Inject constructor(@ApplicationContext context : Context) : ViewModel() {
    private val ekaCareRepository = EkaCareRepository(context)

    private val _userDetails = MutableSharedFlow<List<UserDetails>>(replay = 1)
    val userDetails : SharedFlow<List<UserDetails>> = _userDetails.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ekaCareRepository.userDetails.collectLatest {
                _userDetails.emit(it)
            }
        }
    }

    fun fetchAllUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            ekaCareRepository.fetchAllUserDetailsFromDB()
        }
    }

    fun insertDetailsIntoDB(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            ekaCareRepository.insertUserDetailsIntoDB(userDetails)
        }
    }

    fun deleteUserDetailsFromDB(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            ekaCareRepository.deleteUserDetailsFromDB(userDetails)
        }
    }
}