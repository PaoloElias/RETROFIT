package com.upb.pokeapi.ui.directorio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upb.pokeapi.ui.components.MensajeDeError

@Composable
fun DirectorioScreen(viewModel: DirectorioViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()
    val error = estado.error

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = estado.texto,
                onValueChange = viewModel::cambiarTexto,
                label = { Text("Buscar") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = viewModel::buscar) { Text("Buscar") }
        }

        when {
            estado.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> MensajeDeError(error, onReintentar = viewModel::buscar)
            // "Sin resultados" no es un error
            estado.buscado && estado.usuarios.isEmpty() ->
                Text("Sin resultados", Modifier.padding(top = 16.dp))
            else -> LazyColumn(Modifier.padding(top = 8.dp)) {
                items(estado.usuarios, key = { it.id }) { usuario ->
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(usuario.nombreCompleto, style = MaterialTheme.typography.titleMedium)
                        Text("${usuario.cargo} · ${usuario.area}")
                        usuario.contactos.forEach {
                            Text("${it.tipo}: ${it.valor}${if (it.principal) " (principal)" else ""}")
                        }
                    }
                }
            }
        }
    }
}
