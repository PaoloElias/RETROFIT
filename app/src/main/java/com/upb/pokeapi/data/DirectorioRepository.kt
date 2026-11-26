package com.upb.pokeapi.data

import com.google.gson.Gson
import com.upb.pokeapi.BuildConfig
import com.upb.pokeapi.data.remote.BuscarRequest
import com.upb.pokeapi.data.remote.ContactoDto
import com.upb.pokeapi.data.remote.DirectorioService
import com.upb.pokeapi.data.remote.RetrofitClient
import com.upb.pokeapi.data.remote.listaDesdeTexto
import com.upb.pokeapi.model.Contacto
import com.upb.pokeapi.model.Usuario
import com.upb.pokeapi.util.Resultado
import com.upb.pokeapi.util.llamarApi

const val TAMANO_PAGINA_DIRECTORIO = 20

fun cuerpoDeBusqueda(texto: String, area: String = "", pagina: Int = 0): BuscarRequest =
    BuscarRequest(
        apikey = BuildConfig.API_KEY,
        q = texto,
        area = area,
        limit = TAMANO_PAGINA_DIRECTORIO.toString(),
        offset = (pagina * TAMANO_PAGINA_DIRECTORIO).toString()
    )

class DirectorioRepository(
    private val api: DirectorioService = RetrofitClient.directorio,
    private val gson: Gson = Gson()
) {
    suspend fun buscar(texto: String, area: String = "", pagina: Int = 0): Resultado<List<Usuario>> =
        llamarApi {
            api.buscar(cuerpoDeBusqueda(texto, area, pagina)).data.map { dto ->
                // Segunda lectura: el campo "contactos" es un texto con JSON adentro.
                val contactos = gson.listaDesdeTexto<ContactoDto>(dto.contactos)
                Usuario(
                    id = dto.id,
                    nombreCompleto = dto.nombreCompleto,
                    cargo = dto.cargo.orEmpty(),
                    area = dto.areaCodigo.orEmpty(),
                    contactos = contactos.map { Contacto(it.tipo, it.valor, it.principal == 1) }
                )
            }
        }
}
