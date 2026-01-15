package com.example.virtualcardexample

import kotlinx.coroutines.delay

private const val LOGIN_DELAY_MS = 1500L
private const val MIN_PASSWORD_LENGTH = 6

interface LoginService {
    suspend fun login(username: String, password: String): Result<Unit>
}

class LoginServiceStub : LoginService {
    override suspend fun login(username: String, password: String): Result<Unit> {
        delay(LOGIN_DELAY_MS) // Simulate network delay
        return if (username.isNotBlank() && password.length >= MIN_PASSWORD_LENGTH) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}
