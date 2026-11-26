package com.upb.pokeapi.ui.publicar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PublicarScreen(viewModel: PublicarViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = estado.titulo,
            onValueChange = viewModel::cambiarTitulo,
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = estado.contenido,
            onValueChange = viewModel::cambiarContenido,
            label = { Text("Contenido") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Button(
            onClick = viewModel::enviar,
            enabled = !estado.enviando,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Enviar (POST)")
        }

        if (estado.enviando) CircularProgressIndicator(Modifier.padding(top = 16.dp).size(32.dp))
        estado.idCreado?.let {
            Text("¡Creado! El servidor devolvió id = $it", Modifier.padding(top = 16.dp))
        }
        estado.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
