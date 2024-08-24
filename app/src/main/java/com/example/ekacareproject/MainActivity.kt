package com.example.ekacareproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ekacareproject.ui.screens.UserDetailsScreen
import com.example.ekacareproject.ui.theme.EkaCareProjectTheme
import com.example.ekacareproject.viewmodel.EkaCareViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        var showSnackbar by mutableStateOf(false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val ekaCareViewModel : EkaCareViewModel by viewModels()
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            EkaCareProjectTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .statusBarsPadding()
                ) { innerPadding ->
                    UserDetailsScreen(ekaCareViewModel,innerPadding)

                    LaunchedEffect(showSnackbar) {
                        if(showSnackbar) {
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Field's can't be empty",
                                    actionLabel = "Ok"
                                )

                                when(result) {
                                    SnackbarResult.Dismissed -> showSnackbar = false
                                    SnackbarResult.ActionPerformed -> showSnackbar = false
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EkaCareProjectTheme {
        Greeting("Android")
    }
}