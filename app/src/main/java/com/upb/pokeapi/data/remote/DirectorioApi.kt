package com.upb.pokeapi.data.remote

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import retrofit2.http.Body
import retrofit2.http.POST

// a) Todas son POST y la apikey va en el cuerpo; los parametros viajan como texto.
data class BuscarRequest(
    val apikey: String,
    val q: String,
    val area: String,
    val limit: String,
    val offset: String
)

// b) La respuesta llega envuelta en "data". Sirve para todas las funciones.
data class RespuestaApi<T>(
    val data: List<T>
)

// c) "contactos" llega como TEXTO que contiene un JSON.
data class UsuarioDto(
    val id: Int,
    @SerializedName("nombre_completo") val nombreCompleto: String,
    val cargo: String?,
    @SerializedName("area_codigo") val areaCodigo: String?,
    val contactos: String? // OJO: llega como TEXTO que contiene un JSON, no como lista
)

data class ContactoDto(
    val tipo: String,
    val valor: String,
    val principal: Int
)

interface DirectorioService {
    // La URL completa es baseUrl + "directorio_buscar"
    @POST("directorio_buscar")
    suspend fun buscar(@Body cuerpo: BuscarRequest): RespuestaApi<UsuarioDto>
}

/**
 * Segunda lectura: convierte un TEXTO con JSON en una lista de objetos.
 * Si el texto no sirve (nulo o vacio) devuelve lista vacia.
 * Si no es un JSON valido, Gson lanza JsonSyntaxException (la atrapa el repositorio).
 */
inline fun <reified T> Gson.listaDesdeTexto(texto: String?): List<T> {
    if (texto.isNullOrBlank()) return emptyList()
    val tipo = TypeToken.getParameterized(List::class.java, T::class.java).type
    return fromJson(texto, tipo)
}
