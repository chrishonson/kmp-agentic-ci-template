package com.example.virtualcardexample

import kotlinx.coroutines.delay

interface LoginService {
    suspend fun login(username: String, password: String): Result<Unit>
}

class LoginServiceStub : LoginService {
    override suspend fun login(username: String, password: String): Result<Unit> {
        delay(1500) // Simulate network delay
        return if (username.isNotBlank() && password.length >= 6) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}
