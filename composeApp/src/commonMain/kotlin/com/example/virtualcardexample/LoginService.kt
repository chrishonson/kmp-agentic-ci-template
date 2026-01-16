package com.example.virtualcardexample

import kotlinx.coroutines.delay

private const val LOGIN_DELAY_MS = 1500L
private const val MIN_PASSWORD_LENGTH = 6

data class UserInfo(val name: String)

interface LoginService {
    suspend fun login(username: String, password: String): Result<UserInfo>
    suspend fun loginWithGoogle(): Result<UserInfo>
    suspend fun loginWithFacebook(): Result<UserInfo>
    suspend fun loginWithApple(): Result<UserInfo>
}

class LoginServiceStub : LoginService {
    override suspend fun login(username: String, password: String): Result<UserInfo> {
        delay(LOGIN_DELAY_MS) // Simulate network delay
        return if (username.isNotBlank() && password.length >= MIN_PASSWORD_LENGTH) {
            Result.success(UserInfo(username))
        }
        else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun loginWithGoogle(): Result<UserInfo> {
        delay(LOGIN_DELAY_MS) // Simulate network delay
        return Result.success(UserInfo("Sarah"))
    }

    override suspend fun loginWithFacebook(): Result<UserInfo> {
        delay(LOGIN_DELAY_MS) // Simulate network delay
        return Result.success(UserInfo("Emily"))
    }

    override suspend fun loginWithApple(): Result<UserInfo> {
        delay(LOGIN_DELAY_MS) // Simulate network delay
        return Result.success(UserInfo("Jessica"))
    }
}
