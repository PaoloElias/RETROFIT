package com.upb.pokeapi.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.pokeapi.data.PokemonRepository
import com.upb.pokeapi.model.PokemonDetalle
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleUiState(
    val cargando: Boolean = false,
    val pokemon: PokemonDetalle? = null,
    val error: String? = null
)

/** Ejercicio 1: detalle con @Path. El nombre llega como argumento de navegacion. */
class DetallePokemonViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repositorio = PokemonRepository()
    private val nombre: String = checkNotNull(savedStateHandle["nombre"])

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = repositorio.obtenerPokemon(nombre)) {
                is Resultado.Exito ->
                    _uiState.update { it.copy(cargando = false, pokemon = resultado.datos) }
                is Resultado.Error ->
                    _uiState.update { it.copy(cargando = false, error = resultado.mensaje) }
            }
        }
    }
}
