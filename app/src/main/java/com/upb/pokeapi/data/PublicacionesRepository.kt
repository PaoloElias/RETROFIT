package com.upb.pokeapi.data

import com.upb.pokeapi.data.remote.NuevaPublicacionRequest
import com.upb.pokeapi.data.remote.PublicacionDto
import com.upb.pokeapi.data.remote.PublicacionesService
import com.upb.pokeapi.data.remote.RetrofitClient
import com.upb.pokeapi.util.Resultado
import com.upb.pokeapi.util.llamarApi

class PublicacionesRepository(
    private val api: PublicacionesService = RetrofitClient.publicaciones
) {
    suspend fun listar(usuarioId: Int): Resultado<List<PublicacionDto>> =
        llamarApi { api.listar(usuarioId) }

    suspend fun crear(titulo: String, contenido: String, usuarioId: Int = 1): Resultado<PublicacionDto> =
        llamarApi { api.crear(NuevaPublicacionRequest(titulo, contenido, usuarioId)) }

    suspend fun reemplazar(publicacion: PublicacionDto): Resultado<PublicacionDto> =
        llamarApi { api.reemplazar(publicacion.id, publicacion) }

    /** Devuelve el codigo HTTP (200 si JSONPlaceholder "borro"). */
    suspend fun eliminar(id: Int): Resultado<Int> =
        llamarApi { api.eliminar(id).code() }
}
