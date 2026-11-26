package com.upb.pokeapi.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.upb.pokeapi.ui.components.MensajeDeError

@Composable
fun DetallePokemonScreen(viewModel: DetallePokemonViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()
    val error = estado.error
    val pokemon = estado.pokemon

    when {
        estado.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        error != null -> MensajeDeError(mensaje = error, onReintentar = viewModel::cargar)
        pokemon != null -> Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.imagenUrl,
                contentDescription = pokemon.nombre,
                modifier = Modifier.size(160.dp)
            )
            Text("#${pokemon.id} ${pokemon.nombre}", style = MaterialTheme.typography.headlineMedium)
            Text("Tipos: ${pokemon.tipos.joinToString()}")
            Text("Altura: ${pokemon.alturaMetros} m · Peso: ${pokemon.pesoKg} kg")
            Column(Modifier.padding(top = 16.dp)) {
                pokemon.estadisticas.forEach { Text("${it.nombre}: ${it.valor}") }
            }
        }
    }
}
