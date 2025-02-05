package com.eunsong.receiptreader.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eunsong.camerax.CameraPreview

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is HomeState.Idle -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { viewModel.handleIntent(HomeIntent.OpenCamera) }) {
                    Text("Open Camera")
                }
            }
        }

        is HomeState.CameraOpen -> {
            CameraPreview(
                onImageCaptured = { bitmap ->
                    viewModel.handleIntent(HomeIntent.CaptureImage(bitmap))
                },
                onError = { error ->
                    viewModel.handleIntent(HomeIntent.ShowError(error.message ?: "Unknown error"))
                }
            )
        }

        is HomeState.ImageCaptured -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = (state as HomeState.ImageCaptured).bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier.size(200.dp)
                )
                Button(onClick = { viewModel.handleIntent(HomeIntent.CloseCamera) }) {
                    Text("Back")
                }
            }
        }

        is HomeState.Error -> {
            val errorMessage = (state as HomeState.Error).message
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error: $errorMessage", color = Color.Red)
                Button(onClick = { viewModel.handleIntent(HomeIntent.CloseCamera) }) {
                    Text("Back")
                }
            }
        }
    }
}
