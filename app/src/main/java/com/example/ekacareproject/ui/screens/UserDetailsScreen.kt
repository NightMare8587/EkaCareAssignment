package com.example.ekacareproject.ui.screens

import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.ekacareproject.MainActivity
import com.example.ekacareproject.model.UserDetails
import com.example.ekacareproject.utils.DateUtils
import com.example.ekacareproject.viewmodel.EkaCareViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

@Composable
fun UserDetailsScreen(ekaCareVM: EkaCareViewModel, innerPadding: PaddingValues) {
    var userDetailsList by remember { mutableStateOf(listOf<UserDetails>()) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            ekaCareVM.userDetails.collectLatest {
                userDetailsList = it
            }
        }
    }
    ekaCareVM.fetchAllUserDetails()

    UserDetails(innerPadding,userDetailsList,ekaCareVM)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDetails(innerPadding: PaddingValues, userDetailsList : List<UserDetails>, viewModel: EkaCareViewModel) {
    val lazyListState = rememberLazyListState()
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var showDate by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState()
    dateOfBirth = dateState.selectedDateMillis.let { selectedDate ->
        DateUtils.dateToString(DateUtils.convertMillisToLocalDate(selectedDate ?: System.currentTimeMillis()))
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = name,
            onValueChange = {
                change -> name = change
            },
            placeholder = { Text("Enter your name") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = age,
            onValueChange = {
                    change  -> age = change
            },
            placeholder = { Text("Enter your age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = dateOfBirth,
            enabled = false,
            readOnly = true,
            onValueChange = {

            },
            placeholder = { Text("Select your DOB") },
            trailingIcon = {
                IconButton(onClick = {
                    showDate = true
                }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Picker")
                }
            }
        )

        if(showDate) {
            Popup(
                onDismissRequest = {
                    showDate = false
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = dateState,
                        showModeToggle = false,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = address,
            onValueChange = {
                change -> address = change
            },
            placeholder = { Text("Enter your address") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {

            if(name.isEmpty() || age.isEmpty() || dateOfBirth.isEmpty() || address.isEmpty()) {
                MainActivity.showSnackbar = true
                return@Button
            }

            viewModel.insertDetailsIntoDB(
                UserDetails(
                    UUID.randomUUID().toString(),
                    name,
                    age,
                    dateOfBirth,
                    address
                )
            )

            name = ""
            age = ""
            address = ""
        }) {
            Text("Save Details")
        }

        if(userDetailsList.isNotEmpty()) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(userDetailsList) { index, item ->
                    UserDetailsCard(item, ekaCareVM = viewModel)
                }
            }
        }
    }
}

@Composable
private fun UserDetailsCard(userDetails: UserDetails, ekaCareVM: EkaCareViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            horizontal = 10.dp,
            vertical = 4.dp
        ), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(0.85F)) {
            Text("Name: ${userDetails.name}")
            Text("Age: ${userDetails.age}")
            Text("DOB: ${userDetails.dob}")
            Text("Address: ${userDetails.address}")
        }
        Image(imageVector = Icons.Filled.Delete, contentDescription = null, modifier = Modifier.clickable {
            ekaCareVM.deleteUserDetailsFromDB(userDetails)
        })
    }
}