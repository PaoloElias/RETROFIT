package com.upb.pokeapi.data.remote

/** Una sola instancia de Retrofit por baseUrl. */
object RetrofitClient {

    // Deben terminar en "/" para que Retrofit pueda concatenar las rutas relativas.
    private const val BASE_URL_POKEAPI = "https://pokeapi.co/api/v2/"
    private const val BASE_URL_PUBLICACIONES = "https://jsonplaceholder.typicode.com/"
    private const val BASE_URL_CURSO = "https://clgqxx5idk.g4.sqlite.cloud:443/v2/functions/"

    val api: PokeApiService by lazy {
        FabricaRetrofit.crear(BASE_URL_POKEAPI).create(PokeApiService::class.java)
    }

    val publicaciones: PublicacionesService by lazy {
        FabricaRetrofit.crear(BASE_URL_PUBLICACIONES).create(PublicacionesService::class.java)
    }

    val directorio: DirectorioService by lazy {
        FabricaRetrofit.crear(BASE_URL_CURSO).create(DirectorioService::class.java)
    }
}
