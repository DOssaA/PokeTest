package com.darioossa.poketest.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.GMobiledata
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.testTag
import com.darioossa.poketest.R
import com.darioossa.poketest.ui.theme.LoginAccent
import com.darioossa.poketest.ui.theme.LoginBackground
import com.darioossa.poketest.ui.theme.LoginCard
import com.darioossa.poketest.ui.theme.LoginGradientEnd
import com.darioossa.poketest.ui.theme.LoginGradientStart
import com.darioossa.poketest.ui.theme.LoginInputBackground
import com.darioossa.poketest.ui.theme.LoginMutedText
import com.darioossa.poketest.ui.theme.LoginOutline

@Composable
fun LoginScreen(
    state: LoginState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBiometricClick: () -> Unit,
    onGoogleClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopWelcomeSection()
            FormCard(
                state = state,
                onUsernameChange = onUsernameChange,
                onPasswordChange = onPasswordChange,
                onSubmit = onSubmit,
                onBiometricClick = onBiometricClick,
                onGoogleClick = onGoogleClick
            )
        }
    }
}

@Composable
fun TopWelcomeSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(LoginGradientStart, LoginGradientEnd)
                )
            )
            .padding(horizontal = 24.dp, vertical = 22.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color(0xFF6EC9B7))
                .align(Alignment.TopStart)
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 70.dp, top = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF7C948))
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF28B4B))
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CD964))
            )
        }
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
                .align(Alignment.TopEnd)
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.18f))
                .align(Alignment.TopEnd)
                .padding(end = 34.dp, top = 38.dp)
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_welcome),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.login_subtitle),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun FormCard(
    state: LoginState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBiometricClick: () -> Unit,
    onGoogleClick: () -> Unit
) {
    val errorText = remember(state.error) {
        state.error?.fallbackMessage?.takeIf { it.isNotBlank() }
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = LoginCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_sign_in),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2A2A33),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChange,
                label = { Text(stringResource(id = R.string.login_username)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginScreenTags.UsernameInput),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LoginAccent,
                    unfocusedBorderColor = LoginOutline,
                    focusedContainerColor = LoginInputBackground,
                    unfocusedContainerColor = LoginInputBackground
                )
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(id = R.string.login_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(LoginScreenTags.PasswordInput),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LoginAccent,
                    unfocusedBorderColor = LoginOutline,
                    focusedContainerColor = LoginInputBackground,
                    unfocusedContainerColor = LoginInputBackground
                )
            )

            AnimatedVisibility(
                visible = state.error != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = errorText ?: stringResource(id = state.error?.messageResId ?: R.string.login_error_generic),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.testTag(LoginScreenTags.ErrorText)
                )
            }

            Button(
                onClick = onSubmit,
                enabled = !state.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag(LoginScreenTags.SubmitButton),
                colors = ButtonDefaults.buttonColors(containerColor = LoginAccent),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = if (state.isSubmitting) {
                        stringResource(id = R.string.login_signing_in)
                    } else {
                        stringResource(id = R.string.login_sign_in)
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = LoginOutline)
                Text(
                    text = stringResource(id = R.string.login_continue_with),
                    style = MaterialTheme.typography.labelSmall,
                    color = LoginMutedText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(modifier = Modifier.weight(1f), color = LoginOutline)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onGoogleClick,
                    enabled = !state.isSubmitting,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(BorderStroke(1.dp, LoginOutline), RoundedCornerShape(16.dp))
                        .testTag(LoginScreenTags.GoogleButton),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GMobiledata,
                        contentDescription = stringResource(id = R.string.login_google_cd),
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF3C4043)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.login_google),
                        color = Color(0xFF3C4043)
                    )
                }
                TextButton(
                    onClick = onBiometricClick,
                    enabled = !state.isSubmitting,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, LoginOutline, CircleShape)
                        .testTag(LoginScreenTags.BiometricButton)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = stringResource(id = R.string.login_biometric_cd),
                        modifier = Modifier.size(20.dp),
                        tint = LoginAccent
                    )
                }
            }
        }
    }
}

object LoginScreenTags {
    const val UsernameInput = "login_username"
    const val PasswordInput = "login_password"
    const val SubmitButton = "login_submit"
    const val ErrorText = "login_error"
    const val BiometricButton = "login_biometric"
    const val GoogleButton = "login_google"
}
