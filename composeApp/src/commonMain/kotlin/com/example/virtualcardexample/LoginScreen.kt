package com.example.virtualcardexample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private const val SCREEN_PADDING = 32
private const val SPACER_TINY = 8
private const val SPACER_LARGE = 48
private const val CORNER_RADIUS = 12
private const val SPACER_MEDIUM = 16
private const val SPACER_BUTTON = 24
private const val BUTTON_HEIGHT = 56
private const val PROGRESS_SIZE = 24
private const val STROKE_WIDTH = 2
private const val OPACITY_LOW = 0.6f
private const val OPACITY_BORDER = 0.2f
private const val FACEBOOK_COLOR_HEX = 0xFF1877F2
private val FACEBOOK_COLOR = Color(FACEBOOK_COLOR_HEX)

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val loginService = remember { LoginServiceStub() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SCREEN_PADDING.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader()

            Spacer(modifier = Modifier.height(SPACER_LARGE.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(CORNER_RADIUS.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = OPACITY_BORDER)
                )
            )

            Spacer(modifier = Modifier.height(SPACER_MEDIUM.dp))

            val visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "HIDE" else "SHOW",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(CORNER_RADIUS.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = OPACITY_BORDER)
                )
            )

            Spacer(modifier = Modifier.height(SPACER_BUTTON.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = SPACER_MEDIUM.dp)
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        val result = loginService.login(username, password)
                        isLoading = false
                        if (result.isSuccess) {
                            onLoginSuccess()
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BUTTON_HEIGHT.dp),
                shape = RoundedCornerShape(CORNER_RADIUS.dp),
                enabled = !isLoading && username.isNotBlank() && password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(PROGRESS_SIZE.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = STROKE_WIDTH.dp
                    )
                } else {
                    Text(
                        text = "LOGIN",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(SPACER_MEDIUM.dp))

            Text(
                text = "OR",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = OPACITY_LOW)
            )

            Spacer(modifier = Modifier.height(SPACER_MEDIUM.dp))

            SocialLoginButtons(
                isLoading = isLoading,
                loginService = loginService,
                scope = scope,
                onLoginSuccess = onLoginSuccess,
                onError = { errorMessage = it }
            )
        }
    }
}

@Composable
private fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(SPACER_TINY.dp))

        Text(
            text = "Sign in to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = OPACITY_LOW)
        )
    }
}

@Composable
private fun SocialLoginButtons(
    isLoading: Boolean,
    loginService: LoginService,
    scope: kotlinx.coroutines.CoroutineScope,
    onLoginSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    Column {
        Button(
            onClick = {
                scope.launch {
                    val result = loginService.loginWithGoogle()
                    if (result.isSuccess) {
                        onLoginSuccess()
                    } else {
                        onError(result.exceptionOrNull()?.message ?: "Google login failed")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT.dp),
            shape = RoundedCornerShape(CORNER_RADIUS.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "LOGIN WITH GOOGLE",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(SPACER_MEDIUM.dp))

        Button(
            onClick = {
                scope.launch {
                    val result = loginService.loginWithFacebook()
                    if (result.isSuccess) {
                        onLoginSuccess()
                    } else {
                        onError(result.exceptionOrNull()?.message ?: "Facebook login failed")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT.dp),
            shape = RoundedCornerShape(CORNER_RADIUS.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = FACEBOOK_COLOR
            )
        ) {
            Text(
                text = "LOGIN WITH FACEBOOK",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
