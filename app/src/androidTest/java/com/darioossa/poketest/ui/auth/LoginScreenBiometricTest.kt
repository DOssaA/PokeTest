package com.darioossa.poketest.ui.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginScreenBiometricTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun biometricButtonIsShown() {
        composeRule.setContent {
            LoginScreen(
                state = LoginState(),
                onUsernameChange = {},
                onPasswordChange = {},
                onSubmit = {},
                onBiometricClick = {},
                onGoogleClick = {}
            )
        }

        composeRule.onNodeWithTag(LoginScreenTags.BiometricButton).assertIsDisplayed()
    }

    @Test
    fun biometricButtonInvokesCallback() {
        var count = 0

        composeRule.setContent {
            LoginScreen(
                state = LoginState(),
                onUsernameChange = {},
                onPasswordChange = {},
                onSubmit = {},
                onBiometricClick = { count++ },
                onGoogleClick = {}
            )
        }

        composeRule.onNodeWithTag(LoginScreenTags.BiometricButton).performClick()

        assertEquals(1, count)
    }

    @Test
    fun biometricButtonDisabledWhenSubmitting() {
        composeRule.setContent {
            LoginScreen(
                state = LoginState(isSubmitting = true),
                onUsernameChange = {},
                onPasswordChange = {},
                onSubmit = {},
                onBiometricClick = {},
                onGoogleClick = {}
            )
        }

        composeRule.onNodeWithTag(LoginScreenTags.BiometricButton).assertIsNotEnabled()
    }
}
