package com.achmadichzan.dicodingstory.presentation.ui.screen.auth

import android.util.Patterns
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.achmadichzan.dicodingstory.R
import com.achmadichzan.dicodingstory.presentation.ui.screen.auth.components.EmailTextField
import com.achmadichzan.dicodingstory.presentation.ui.screen.auth.components.PasswordTextField
import com.achmadichzan.dicodingstory.presentation.state.RegisterState
import com.achmadichzan.dicodingstory.presentation.intent.RegisterIntent

@Composable
fun RegisterScreen(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
) {

    val infiniteTransition = rememberInfiniteTransition(label = "slide")

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )

    val autoFill = LocalAutofillManager.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

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
                modifier = Modifier.offset(x = offsetX.dp),
                painter = painterResource(R.drawable.image_signup),
                contentScale = ContentScale.FillWidth,
                contentDescription = "Login Image"
            )
            Text(
                text = "Lengkapi data dirimu di bawah ini ya",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama") },
                supportingText = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Name Icon"
                    )
                },
                maxLines = 2,
                shape = RoundedCornerShape(10.dp)
            )

            EmailTextField(
                email = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isEmpty()) {
                        "Email tidak boleh kosong"
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                        "Alamat email tidak valid"
                    } else { null }
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
                supportingText = { if (passwordError != null) { Text(passwordError!!) } },
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                keyboardActions = KeyboardActions(onDone = { autoFill?.commit() }),
                modifier = Modifier.semantics { contentType = ContentType.Password }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onIntent(RegisterIntent.Submit(name, email, password))
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
                    Text("Daftar")
                }
            }

            TextButton(
                onClick = { onIntent(RegisterIntent.NavigateBackToLogin) }
            ) {
                Text("Sudah punya akun? Masuk")
            }

            state.error?.let { errorMessage ->
                Text(
                    errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}