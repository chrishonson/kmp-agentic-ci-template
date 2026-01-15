package com.example.virtualcardexample

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginServiceTest {

    private val loginService = LoginServiceStub()

    @Test
    fun `test login success`() = runTest {
        val result = loginService.login("user", "password")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `test login failure`() = runTest {
        val result = loginService.login("", "")
        assertTrue(result.isFailure)
    }

    @Test
    fun `test loginWithGoogle success`() = runTest {
        val result = loginService.loginWithGoogle()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `test loginWithFacebook success`() = runTest {
        val result = loginService.loginWithFacebook()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `test loginWithApple success`() = runTest {
        val result = loginService.loginWithApple()
        assertTrue(result.isSuccess)
    }
}
