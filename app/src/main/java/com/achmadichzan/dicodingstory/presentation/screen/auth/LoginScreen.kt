package com.achmadichzan.dicodingstory.presentation.screen.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.achmadichzan.dicodingstory.R
import com.achmadichzan.dicodingstory.presentation.navigation.Route
import com.achmadichzan.dicodingstory.presentation.screen.auth.components.EmailTextField
import com.achmadichzan.dicodingstory.presentation.screen.auth.components.PasswordTextField
import com.achmadichzan.dicodingstory.presentation.util.LoginIntent
import com.achmadichzan.dicodingstory.presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state = viewModel.state

    val infiniteTransition = rememberInfiniteTransition(label = "slide")

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )

    val autoFill = LocalAutofillManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.success) {
        state.success?.let {
            navController.navigate(Route.Story) {
                popUpTo(0)
            }
        }
    }

    Scaffold(contentWindowInsets = WindowInsets.safeContent) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.offset(x = offsetX.dp)
                    .padding(top = 16.dp),
                painter = painterResource(R.drawable.image_login),
                contentScale = ContentScale.FillWidth,
                contentDescription = "Login Image",
            )
            Text(
                text = "Sudah siap untuk berbagi cerita?",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Masukkan dulu datamu ya",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))

            EmailTextField(
                email = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isEmpty()) {
                        "Email tidak boleh kosong"
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                        "Alamat email tidak valid"
                    } else {
                        null
                    }
                },
                label = { Text("Email") },
                isError = emailError != null,
                supportingText = { if (emailError != null) { Text(emailError!!) } },
                modifier = Modifier.semantics { contentType = ContentType.EmailAddress }
            )

            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError =
                        if (it.length < 8) { "Kata Sandi harus terdiri dari minimal 8 karakter" }
                        else { null }
                },
                label = { Text("Kata Sandi") },
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = { Text(passwordError ?: "") },
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                keyboardActions = KeyboardActions(
                    onGo = { viewModel.onIntent(LoginIntent.Submit(email, password)) }
                ),
                modifier = Modifier.semantics { contentType = ContentType.Password }
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = {
                    viewModel.onIntent(LoginIntent.Submit(email, password))
                    autoFill?.commit()
                },
                enabled = email.isNotEmpty() && password.isNotEmpty() && !state.isLoading
                        && passwordError == null && emailError == null,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Masuk")
                }
            }

            TextButton(
                onClick = { navController.navigate(Route.Register) {
                    popUpTo(Route.Register) {
                        inclusive = false
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } }
            ) {
                Text("Belum punya akun? Daftar")
            }

            state.error?.let {
                Text(
                    it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
