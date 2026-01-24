package com.darioossa.poketest.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.darioossa.poketest.R
import com.darioossa.poketest.domain.model.GoogleAuthOutcome
import com.darioossa.poketest.ui.PokedexRoutes
import com.darioossa.poketest.ui.theme.LoginGradientEnd
import com.darioossa.poketest.ui.theme.LoginGradientStart
import com.darioossa.poketest.util.biometric.BiometricPromptManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthEntryScreen(
    navController: NavHostController,
    biometricPromptManager: BiometricPromptManager,
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val loginState by loginViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var navigateHome by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(context) {
        val activity = context as? FragmentActivity
        activity?.let { biometricPromptManager.bind(it) }
    }

    LaunchedEffect(Unit) {
        loginViewModel.effect.collectLatest { effect ->
            if (effect is LoginEffect.NavigateHome) {
                navigateHome = true
            }
        }
    }

    LaunchedEffect(navigateHome) {
        if (navigateHome) {
            delay(320)
            navController.navigate(PokedexRoutes.Pokedex) {
                popUpTo(PokedexRoutes.Login) { inclusive = true }
            }
        }
    }

    AnimatedContent(
        targetState = navigateHome,
        transitionSpec = {
            (fadeIn() + scaleIn(initialScale = 0.98f)).togetherWith(
                fadeOut() + scaleOut(targetScale = 1.02f)
            )
        },
        label = "login-entry"
    ) { isNavigating ->
        if (isNavigating) {
            LoginTransitionScreen()
        } else {
            when (loginState.screen) {
                LoginScreenStep.GOOGLE -> {
                    GoogleOAuthScreen(
                        isSubmitting = loginState.isSubmitting,
                        errorMessage = loginState.error?.fallbackMessage
                            ?: loginState.error?.messageResId?.let { stringResource(id = it) },
                        onContinue = { loginViewModel.onGoogleContinue(GoogleAuthOutcome.LOADING, delayMs = 1200) },
                        onCancel = loginViewModel::onGoogleCancel,
                        onError = loginViewModel::onGoogleError
                    )
                }
                LoginScreenStep.LOGIN -> {
                    LoginScreen(
                        state = loginState,
                        onUsernameChange = loginViewModel::onUsernameChange,
                        onPasswordChange = loginViewModel::onPasswordChange,
                        onSubmit = loginViewModel::onSubmit,
                        onBiometricClick = loginViewModel::onBiometricClick,
                        onGoogleClick = loginViewModel::onGoogleClick
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginTransitionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(LoginGradientStart, LoginGradientEnd)
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_welcome),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(id = R.string.login_subtitle),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}
