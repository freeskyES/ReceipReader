package com.eunsong.receiptreader.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eunsong.camerax.CameraPreview
import java.io.FileNotFoundException
import java.io.InputStream

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val imagePath = selectedUri.toString()
            viewModel.handleIntent(HomeIntent.ImageSelected(imagePath))
        }
    }

    when (state) {
        is HomeState.Idle -> IdleScreen(viewModel) { galleryLauncher.launch("image/*") }
        is HomeState.CameraOpen -> CameraScreen(viewModel)
        is HomeState.ImageCaptured -> ImagePreviewScreen((state as HomeState.ImageCaptured).imagePath, viewModel)
        is HomeState.OcrProcessing -> OcrProcessingScreen()
        is HomeState.OcrSuccess -> OcrResultScreen((state as HomeState.OcrSuccess).text)
        is HomeState.Error -> ErrorScreen((state as HomeState.Error).message, viewModel)
    }
}

// 카메라 & 갤러리 선택 화면
@Composable
fun IdleScreen(
    viewModel: HomeViewModel,
    galleryLauncher: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.handleIntent(HomeIntent.OpenCamera) }) {
            Text("📷 카메라 열기")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            galleryLauncher()
        }) {
            Text("🖼️ 갤러리에서 선택")
        }
    }
}

// 카메라 화면 (CameraX)
@Composable
fun CameraScreen(viewModel: HomeViewModel) {
    CameraPreview(
        onImageCaptured = { imagePath ->
            viewModel.handleIntent(HomeIntent.ImageCaptured(imagePath))
        },
        onError = { error ->
            viewModel.handleIntent(HomeIntent.OcrFailed(error.message ?: "알 수 없는 오류"))
        }
    )
}

// 캡처된 이미지 미리보기 화면
@Composable
fun ImagePreviewScreen(imagePath: String, viewModel: HomeViewModel) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromUri(context.contentResolver, Uri.parse(imagePath)) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .size(200.dp)
                    .clickable { viewModel.handleIntent(HomeIntent.ProcessOcr(imagePath)) }
            )
        } ?: Text("이미지를 불러올 수 없습니다.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.handleIntent(HomeIntent.ProcessOcr(imagePath)) }) {
            Text("📑 OCR 분석하기")
        }
    }
}

fun loadBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

// OCR 처리 중 (로딩 화면)
@Composable
fun OcrProcessingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text("OCR 처리 중...")
    }
}

// OCR 결과 화면
@Composable
fun OcrResultScreen(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📜 OCR 결과", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, modifier = Modifier.padding(16.dp))
    }
}

// 오류 화면
@Composable
fun ErrorScreen(message: String, viewModel: HomeViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("❌ 오류 발생: $message", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.handleIntent(HomeIntent.CloseCamera) }) {
            Text("🔄 다시 시도")
        }
    }
}