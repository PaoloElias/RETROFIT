package com.upb.pokeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.upb.pokeapi.ui.detail.DetallePokemonScreen
import com.upb.pokeapi.ui.directorio.DirectorioScreen
import com.upb.pokeapi.ui.list.ListaPokemonScreen
import com.upb.pokeapi.ui.publicar.PublicarScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavegacion()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val nav = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PokeAPI + Retrofit") },
                actions = {
                    TextButton(onClick = { nav.navigate("lista") }) { Text("GET") }
                    TextButton(onClick = { nav.navigate("publicar") }) { Text("POST") }
                    TextButton(onClick = { nav.navigate("directorio") }) { Text("Curso") }
                }
            )
        }
    ) { padding ->
        NavHost(nav, startDestination = "lista", modifier = Modifier.padding(padding)) {
            composable("lista") {
                ListaPokemonScreen(onPokemonClick = { nombre -> nav.navigate("detalle/$nombre") })
            }
            composable(
                route = "detalle/{nombre}",
                arguments = listOf(navArgument("nombre") { type = NavType.StringType })
            ) {
                DetallePokemonScreen()
            }
            composable("publicar") { PublicarScreen() }
            composable("directorio") { DirectorioScreen() }
        }
    }
}
