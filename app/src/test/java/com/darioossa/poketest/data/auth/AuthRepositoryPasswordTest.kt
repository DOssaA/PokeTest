package com.darioossa.poketest.data.auth

import com.darioossa.poketest.data.auth.local.AuthLocalDataSource
import com.darioossa.poketest.data.auth.remote.AuthRemoteDataSource
import com.darioossa.poketest.domain.model.AuthMethod
import com.darioossa.poketest.domain.model.AuthResult
import com.darioossa.poketest.domain.model.CredentialStorageState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class AuthRepositoryPasswordTest {
    @Test
    fun loginWithPasswordStoresCredentialOnSuccess() = runTest {
        val localDataSource = mockk<AuthLocalDataSource>()
        val remoteDataSource = mockk<AuthRemoteDataSource>()
        val credentialSlot = slot<com.darioossa.poketest.domain.model.UserCredential>()
        coEvery { localDataSource.saveCredential(capture(credentialSlot)) } returns true

        val repository = AuthRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.loginWithPassword("ash", "pikachu")

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals(AuthMethod.PASSWORD, success.credential.authMethod)
        assertEquals("ash", success.credential.username)
        assertEquals(CredentialStorageState.STORED, success.credential.storageState)
        assertEquals("ash", credentialSlot.captured.username)
        coVerify(exactly = 1) { localDataSource.saveCredential(any()) }
    }

    @Test
    fun loginWithPasswordReturnsErrorWhenStorageFails() = runTest {
        val localDataSource = mockk<AuthLocalDataSource>()
        val remoteDataSource = mockk<AuthRemoteDataSource>()
        coEvery { localDataSource.saveCredential(any()) } returns false

        val repository = AuthRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.loginWithPassword("ash", "pikachu")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(AuthMethod.PASSWORD, error.method)
        assertEquals("STORAGE_FAILED", error.code)
    }

    @Test
    fun loginWithPasswordRejectsBlankUsername() = runTest {
        val localDataSource = mockk<AuthLocalDataSource>()
        val remoteDataSource = mockk<AuthRemoteDataSource>()

        val repository = AuthRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.loginWithPassword(" ", "pikachu")

        assertTrue(result is AuthResult.Error)
        coVerify(exactly = 0) { localDataSource.saveCredential(any()) }
    }

    @Test
    fun loginWithPasswordTrimsUsernameBeforeStorage() = runTest {
        val localDataSource = mockk<AuthLocalDataSource>()
        val remoteDataSource = mockk<AuthRemoteDataSource>()
        val credentialSlot = slot<com.darioossa.poketest.domain.model.UserCredential>()
        coEvery { localDataSource.saveCredential(capture(credentialSlot)) } returns true

        val repository = AuthRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.loginWithPassword(" ash ", "pikachu")

        assertFalse(result is AuthResult.Error)
        assertEquals("ash", credentialSlot.captured.username)
    }
}
