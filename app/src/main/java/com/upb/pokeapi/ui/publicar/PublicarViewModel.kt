package com.upb.pokeapi.ui.publicar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.pokeapi.data.PublicacionesRepository
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PublicarUiState(
    val titulo: String = "",
    val contenido: String = "",
    val enviando: Boolean = false,
    val idCreado: Int? = null,
    val error: String? = null
)

/** Ejercicio 3: formulario que hace POST a JSONPlaceholder y muestra el id devuelto. */
class PublicarViewModel(
    private val repositorio: PublicacionesRepository = PublicacionesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublicarUiState())
    val uiState: StateFlow<PublicarUiState> = _uiState.asStateFlow()

    fun cambiarTitulo(valor: String) = _uiState.update { it.copy(titulo = valor) }
    fun cambiarContenido(valor: String) = _uiState.update { it.copy(contenido = valor) }

    fun enviar() {
        val actual = _uiState.value
        if (actual.enviando || actual.titulo.isBlank() || actual.contenido.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(enviando = true, error = null, idCreado = null) }
            when (val resultado = repositorio.crear(actual.titulo, actual.contenido)) {
                is Resultado.Exito ->
                    _uiState.update { it.copy(enviando = false, idCreado = resultado.datos.id) }
                is Resultado.Error ->
                    _uiState.update { it.copy(enviando = false, error = resultado.mensaje) }
            }
        }
    }
}
