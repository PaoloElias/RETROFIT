package com.upb.pokeapi.data.remote

import com.google.gson.annotations.SerializedName
import com.upb.pokeapi.model.Estadistica
import com.upb.pokeapi.model.PokemonDetalle
import com.upb.pokeapi.model.PokemonResumen

private const val URL_IMAGENES =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"

// GET https://pokeapi.co/api/v2/pokemon?limit=20&offset=0
data class PokemonListResponse(
    val count: Int,
    val next: String?, // puede ser null: por eso lleva "?"
    val results: List<PokemonItemDto>
)

data class PokemonItemDto(
    val name: String,
    val url: String
)

// GET https://pokeapi.co/api/v2/pokemon/pikachu  (solo los campos que usamos)
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int, // decimetros
    val weight: Int, // hectogramos
    val sprites: SpritesDto?,
    val stats: List<StatSlotDto>,
    val types: List<TypeSlotDto>
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?
)

data class StatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedDto
)

data class TypeSlotDto(
    val slot: Int,
    val type: NamedDto
)

data class NamedDto(val name: String)

// ---- Mapeo DTO -> modelo del dominio (si la API cambia, solo se toca aqui) ----

fun PokemonItemDto.aResumen(): PokemonResumen {
    val id = url.trimEnd('/').substringAfterLast('/').toInt() // ".../pokemon/25/" -> 25
    return PokemonResumen(id = id, nombre = name, imagenUrl = "$URL_IMAGENES$id.png")
}

fun PokemonDetailDto.aDetalle(): PokemonDetalle =
    PokemonDetalle(
        id = id,
        nombre = name,
        alturaMetros = height / 10.0,
        pesoKg = weight / 10.0,
        imagenUrl = sprites?.frontDefault ?: "$URL_IMAGENES$id.png",
        tipos = types.map { it.type.name },
        estadisticas = stats.map { Estadistica(it.stat.name, it.baseStat) }
    )
