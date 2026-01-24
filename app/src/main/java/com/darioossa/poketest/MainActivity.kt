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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeTestTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        PokedexNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
