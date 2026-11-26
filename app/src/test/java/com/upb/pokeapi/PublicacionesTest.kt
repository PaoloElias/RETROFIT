package com.upb.pokeapi

import com.upb.pokeapi.data.PokemonRepository
import com.upb.pokeapi.data.remote.FabricaRetrofit
import com.upb.pokeapi.data.remote.NuevaPublicacionRequest
import com.upb.pokeapi.data.remote.PokeApiService
import com.upb.pokeapi.data.remote.PublicacionesService
import com.upb.pokeapi.data.remote.DirectorioService
import com.upb.pokeapi.data.remote.BuscarRequest
import com.upb.pokeapi.data.DirectorioRepository
import com.upb.pokeapi.model.PokemonResumen
import com.upb.pokeapi.util.Resultado
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PublicacionesTest {

    private lateinit var servidor: MockWebServer

    @Before fun preparar() { servidor = MockWebServer().apply { start() } }
    @After fun cerrar() { runCatching { servidor.shutdown() } }

    private fun urlBase() = servidor.url("/").toString()

    @Test fun post_envia_el_objeto_como_json_en_el_cuerpo() = runBlocking {
        servidor.enqueue(
            MockResponse().addHeader("Content-Type", "application/json")
                .setBody("""{"title":"Hola","body":"Mi primer POST","userId":1,"id":101}""")
        )
        val api = FabricaRetrofit.crear(urlBase()).create(PublicacionesService::class.java)

        val creada = api.crear(NuevaPublicacionRequest("Hola", "Mi primer POST", 1))

        val peticion = servidor.takeRequest()
        assertEquals("POST", peticion.method)
        assertEquals("/posts", peticion.path)
        assertEquals("""{"title":"Hola","body":"Mi primer POST","userId":1}""", peticion.body.readUtf8())
        assertEquals(101, creada.id)
    }

    // ---- Ejercicio 5: 404, servidor caido y lista vacia en "data" ----

    @Test fun respuesta_404_se_traduce_a_mensaje_con_el_codigo() = runBlocking {
        servidor.enqueue(MockResponse().setResponseCode(404).setBody("{}"))
        val repo = PokemonRepository(FabricaRetrofit.crear(urlBase()).create(PokeApiService::class.java))

        val resultado = repo.obtenerPokemones(20, 0)

        assertTrue(resultado is Resultado.Error)
        assertTrue((resultado as Resultado.Error).mensaje.contains("404"))
    }

    @Test fun servidor_caido_se_traduce_a_error_de_conexion() = runBlocking {
        val repo = PokemonRepository(FabricaRetrofit.crear(urlBase()).create(PokeApiService::class.java))
        servidor.shutdown() // simula el servidor caido

        val resultado = repo.obtenerPokemones(20, 0)

        assertTrue(resultado is Resultado.Error)
        assertTrue((resultado as Resultado.Error).mensaje.contains("conexión"))
    }

    @Test fun lista_vacia_en_data_no_es_un_error() = runBlocking {
        servidor.enqueue(
            MockResponse().addHeader("Content-Type", "application/json").setBody("""{"data":[]}""")
        )
        val api = FabricaRetrofit.crear(urlBase()).create(DirectorioService::class.java)
        val repo = DirectorioRepository(api)

        val resultado = repo.buscar("zzz")

        assertTrue(resultado is Resultado.Exito)
        assertEquals(0, (resultado as Resultado.Exito).datos.size)
    }

    @Test fun contactos_en_texto_se_leen_con_segunda_lectura() = runBlocking {
        val json = """{"data":[{"id":4,"nombre_completo":"Lucía Quispe Mamani","cargo":null,"area_codigo":"SIS",""" +
            """"contactos":"[{\"tipo\":\"CELULAR\",\"valor\":\"+59170000003\",\"principal\":1}]"}]}"""
        servidor.enqueue(MockResponse().addHeader("Content-Type", "application/json").setBody(json))
        val repo = DirectorioRepository(FabricaRetrofit.crear(urlBase()).create(DirectorioService::class.java))

        val resultado = repo.buscar("lucia") as Resultado.Exito

        val usuario = resultado.datos.single()
        assertEquals("Lucía Quispe Mamani", usuario.nombreCompleto)
        assertEquals("", usuario.cargo)
        assertEquals("+59170000003", usuario.contactos.single().valor)
        assertTrue(usuario.contactos.single().principal)
    }

    @Test fun lista_de_pokemones_se_mapea_a_resumen_con_id() = runBlocking {
        servidor.enqueue(
            MockResponse().addHeader("Content-Type", "application/json").setBody(
                """{"count":2,"next":null,"results":[{"name":"bulbasaur","url":"https://pokeapi.co/api/v2/pokemon/1/"}]}"""
            )
        )
        val repo = PokemonRepository(FabricaRetrofit.crear(urlBase()).create(PokeApiService::class.java))

        val resultado = repo.obtenerPokemones(1, 0) as Resultado.Exito

        val primero: PokemonResumen = resultado.datos.single()
        assertEquals(1, primero.id)
        assertEquals("bulbasaur", primero.nombre)
        assertEquals("/pokemon?limit=1&offset=0", servidor.takeRequest().path)
    }
}
