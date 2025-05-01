package com.achmadichzan.dicodingstory.presentation.ui.screen.addstory

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.twotone.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.compose.dropUnlessResumed
import coil3.compose.AsyncImage
import com.achmadichzan.dicodingstory.presentation.intent.AddStoryIntent
import com.achmadichzan.dicodingstory.presentation.state.UploadState
import com.achmadichzan.dicodingstory.presentation.ui.screen.addstory.component.CameraXScreen
import com.achmadichzan.dicodingstory.presentation.util.FileUtil
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStoryScreen(
    state: UploadState,
    onUpload: (File, String) -> Unit,
    onIntent: (AddStoryIntent) -> Unit
) {
    val context = LocalContext.current

    var description by remember { mutableStateOf("") }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    var showCameraX by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val animatedProgress by animateFloatAsState(
        targetValue = state.compressingProgress / 100f,
        animationSpec = tween(durationMillis = 300),
        label = "CompressionProgress"
    )

    if (showCameraX) {
        CameraXScreen(
            onImageCaptured = {
                onIntent(AddStoryIntent.PickImage(it.toUri(), context))
                showCameraX = false
            },
            onBack = dropUnlessResumed { showCameraX = false }
        )
    } else {
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            uri?.let {
                onIntent(AddStoryIntent.PickImage(it, context))
            }
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraImageUri.value?.let { uri ->
                    onIntent(AddStoryIntent.PickImage(uri, context))
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
            coroutineScope.launch {
                val tempFile = FileUtil.createImageFile(context)
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempFile
                )
                cameraImageUri.value = uri
                cameraLauncher.launch(uri)
            }
        }

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                onIntent(AddStoryIntent.ToggleLocation(true))
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
                onIntent(AddStoryIntent.ToggleLocation(false))
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { onIntent(AddStoryIntent.GoBack) }) {
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
                    .imePadding()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.isCompressing) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(70.dp),
                        strokeWidth = 8.dp
                    )
                } else {
                    state.imageUri?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    } ?: Icon(
                        imageVector = Icons.TwoTone.AddPhotoAlternate,
                        contentDescription = "Upload Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(200.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = dropUnlessResumed {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                        Text("Gallery")
                    }

                    OutlinedButton(onClick = dropUnlessResumed {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                launchCamera()
                            }
                            else -> {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }) {
                        Text("Camera")
                    }

                    OutlinedButton(onClick = dropUnlessResumed {
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

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .height(100.dp),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Aktifkan Lokasi?")
                    Switch(
                        checked = state.isLocationEnabled,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            } else {
                                onIntent(AddStoryIntent.ToggleLocation(false))
                            }
                        }
                    )
                }

                Button(
                    onClick = dropUnlessResumed {
                        state.selectedFile?.let { onUpload(it, description) }
                    },
                    enabled = state.selectedFile != null && description.isNotEmpty() && !state.isLoading,
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