package com.upb.pokeapi.ui.directorio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.pokeapi.data.DirectorioRepository
import com.upb.pokeapi.model.Usuario
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DirectorioUiState(
    val texto: String = "",
    val cargando: Boolean = false,
    val buscado: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val error: String? = null
)

/** Ejercicio 6: funcion de la API del curso (POST + apikey + data + JSON dentro de texto). */
class DirectorioViewModel(
    private val repositorio: DirectorioRepository = DirectorioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DirectorioUiState())
    val uiState: StateFlow<DirectorioUiState> = _uiState.asStateFlow()

    fun cambiarTexto(valor: String) = _uiState.update { it.copy(texto = valor) }

    fun buscar() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = repositorio.buscar(_uiState.value.texto)) {
                is Resultado.Exito ->
                    _uiState.update {
                        it.copy(cargando = false, buscado = true, usuarios = resultado.datos)
                    }
                is Resultado.Error ->
                    _uiState.update { it.copy(cargando = false, error = resultado.mensaje) }
            }
        }
    }
}
