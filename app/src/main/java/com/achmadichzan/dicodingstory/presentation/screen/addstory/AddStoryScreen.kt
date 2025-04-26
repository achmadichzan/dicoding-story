package com.achmadichzan.dicodingstory.presentation.screen.addstory

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.achmadichzan.dicodingstory.R
import com.achmadichzan.dicodingstory.presentation.components.CameraXScreen
import com.achmadichzan.dicodingstory.presentation.util.FileUtil
import com.achmadichzan.dicodingstory.presentation.viewmodel.UploadViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStoryScreen(
    navController: NavController,
    viewModel: UploadViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state

    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var file by remember { mutableStateOf<File?>(null) }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    var showCameraX by remember { mutableStateOf(false) }

    if (showCameraX) {
        CameraXScreen(
            onImageCaptured = {
                file = it
                imageUri = it.toUri()
                showCameraX = false
            },
            onBack = { showCameraX = false }
        )
    } else {
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            uri?.let {
                imageUri = it
                val originalFile = FileUtil.from(context, it)
                file = FileUtil.compressImageFile(context, originalFile)
            }
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraImageUri.value?.let { uri ->
                    imageUri = uri
                    val originalFile = FileUtil.from(context, uri)
                    file = FileUtil.compressImageFile(context, originalFile)
                }
            }
        }

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showCameraX = true
            } else {
                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        val launchCamera: () -> Unit = {
            val tempFile = FileUtil.createImageFile(context)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                tempFile
            )
            cameraImageUri.value = uri
            file = tempFile
            cameraLauncher.launch(uri)
        }

        LaunchedEffect(state.success) {
            state.success?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Back")
                        }
                    },
                    title = { Text("Upload Story") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxWidth()
                            .height(200.dp)
                    )
                } ?: Image(
                    painter = painterResource(R.drawable.add_photo_50),
                    contentDescription = "Upload Image",
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                        Text("Gallery")
                    }
                    OutlinedButton(onClick = { launchCamera() }) {
                        Text("Camera")
                    }

                    OutlinedButton(onClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                showCameraX = true
                            }
                            else -> {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }) {
                        Text("CameraX")
                    }


                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        file?.let {
                            viewModel.uploadStory(it, description, null, null)
                        }
                    },
                    enabled = file != null && description.isNotEmpty() && !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isLoading) "Uploading..." else "Upload")
                }

                state.error?.let {
                    Text(it, color = Color.Red)
                }
            }
        }
    }
}