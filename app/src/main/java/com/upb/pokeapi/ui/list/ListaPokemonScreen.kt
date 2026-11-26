package com.upb.pokeapi.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
fun ListaPokemonScreen(
    onPokemonClick: (String) -> Unit = {},
    viewModel: ListaPokemonViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val error = estado.error

    when {
        // Primera carga
        estado.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        // Error sin datos que mostrar
        error != null && estado.pokemones.isEmpty() ->
            MensajeDeError(mensaje = error, onReintentar = viewModel::cargar)
        // Datos
        else -> LazyColumn(Modifier.fillMaxSize()) {
            items(estado.pokemones, key = { it.id }) { pokemon ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPokemonClick(pokemon.nombre) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = pokemon.imagenUrl,
                        contentDescription = pokemon.nombre,
                        modifier = Modifier.size(56.dp)
                    )
                    Text("#${pokemon.id} ${pokemon.nombre}")
                }
            }
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        estado.cargandoMas -> CircularProgressIndicator()
                        error != null -> MensajeDeError(error, onReintentar = viewModel::cargarMas)
                        else -> Button(onClick = viewModel::cargarMas) { Text("Cargar más") }
                    }
                }
            }
        }
    }
}
