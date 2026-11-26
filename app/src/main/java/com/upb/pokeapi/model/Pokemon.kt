package com.upb.pokeapi.model

/** Modelo del dominio: lo que la app entiende (no la forma del JSON). */
data class PokemonResumen(
    val id: Int,
    val nombre: String,
    val imagenUrl: String
)

data class Estadistica(val nombre: String, val valor: Int)

data class PokemonDetalle(
    val id: Int,
    val nombre: String,
    val alturaMetros: Double,
    val pesoKg: Double,
    val imagenUrl: String,
    val tipos: List<String>,
    val estadisticas: List<Estadistica>
)

/** Modelo del dominio del directorio (API del curso). */
data class Contacto(val tipo: String, val valor: String, val principal: Boolean)

data class Usuario(
    val id: Int,
    val nombreCompleto: String,
    val cargo: String,
    val area: String,
    val contactos: List<Contacto>
)
