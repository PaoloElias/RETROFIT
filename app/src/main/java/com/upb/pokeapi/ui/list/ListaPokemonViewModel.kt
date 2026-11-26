package com.upb.pokeapi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.pokeapi.data.PokemonRepository
import com.upb.pokeapi.model.PokemonResumen
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAMANO_PAGINA = 20

/** Un solo objeto inmutable con todo lo que la pantalla necesita. */
data class ListaUiState(
    val cargando: Boolean = false,
    val cargandoMas: Boolean = false,
    val pokemones: List<PokemonResumen> = emptyList(),
    val error: String? = null
)

class ListaPokemonViewModel(
    private val repositorio: PokemonRepository = PokemonRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaUiState())
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    /** Carga la primera pagina. Sirve tambien como "Reintentar". */
    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = repositorio.obtenerPokemones(TAMANO_PAGINA, 0)) {
                is Resultado.Exito ->
                    _uiState.update { it.copy(cargando = false, pokemones = resultado.datos) }
                is Resultado.Error ->
                    _uiState.update { it.copy(cargando = false, error = resultado.mensaje) }
            }
        }
    }

    /** Ejercicio 2: "Cargar mas" usando offset para pedir la pagina siguiente. */
    fun cargarMas() {
        val actual = _uiState.value
        if (actual.cargando || actual.cargandoMas) return
        viewModelScope.launch {
            _uiState.update { it.copy(cargandoMas = true, error = null) }
            when (val resultado = repositorio.obtenerPokemones(TAMANO_PAGINA, actual.pokemones.size)) {
                is Resultado.Exito ->
                    _uiState.update {
                        it.copy(cargandoMas = false, pokemones = it.pokemones + resultado.datos)
                    }
                is Resultado.Error ->
                    _uiState.update { it.copy(cargandoMas = false, error = resultado.mensaje) }
            }
        }
    }
}
