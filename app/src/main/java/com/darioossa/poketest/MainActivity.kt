package com.darioossa.poketest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.darioossa.poketest.ui.PokedexNavGraph
import com.darioossa.poketest.ui.theme.PokeTestTheme
import com.darioossa.poketest.util.biometric.BiometricPromptManager
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeTestTheme {
                val navController = rememberNavController()
                val biometricPromptManager: BiometricPromptManager = get()
                Scaffold { innerPadding ->
                    PokedexNavGraph(navController, innerPadding, biometricPromptManager)
                }
            }
        }
    }
}
