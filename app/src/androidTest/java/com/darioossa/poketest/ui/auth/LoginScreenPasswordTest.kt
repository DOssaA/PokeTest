package com.darioossa.poketest.ui.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginScreenPasswordTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginScreenShowsInputsAndSubmit() {
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

        composeRule.onNodeWithTag(LoginScreenTags.UsernameInput).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginScreenTags.PasswordInput).assertIsDisplayed()
        composeRule.onNodeWithTag(LoginScreenTags.SubmitButton).assertIsDisplayed()
    }

    @Test
    fun submitClickInvokesCallback() {
        var submitCount = 0

        composeRule.setContent {
            LoginScreen(
                state = LoginState(),
                onUsernameChange = {},
                onPasswordChange = {},
                onSubmit = { submitCount++ },
                onBiometricClick = {},
                onGoogleClick = {}
            )
        }

        composeRule.onNodeWithTag(LoginScreenTags.SubmitButton).performClick()

        assertEquals(1, submitCount)
    }

    @Test
    fun errorStateShowsErrorMessage() {
        composeRule.setContent {
            LoginScreen(
                state = LoginState(error = LoginError.MissingPassword),
                onUsernameChange = {},
                onPasswordChange = {},
                onSubmit = {},
                onBiometricClick = {},
                onGoogleClick = {}
            )
        }

        composeRule.onNodeWithTag(LoginScreenTags.ErrorText).assertIsDisplayed()
    }

    @Test
    fun submitDisabledWhenSubmitting() {
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

        composeRule.onNodeWithTag(LoginScreenTags.SubmitButton).assertIsNotEnabled()
    }
}
