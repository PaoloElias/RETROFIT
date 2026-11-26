package com.upb.pokeapi.util

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

/** Representa "salio bien" o "salio mal". La pantalla solo decide que dibujar. */
sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}

/**
 * Ejecuta una llamada a la API y traduce las excepciones a un [Resultado].
 * Todos los repositorios usan esta misma estructura try/catch (paso 8 y 14).
 */
suspend fun <T> llamarApi(bloque: suspend () -> T): Resultado<T> =
    try {
        Resultado.Exito(bloque())
    } catch (e: CancellationException) {
        throw e // cancelar la corrutina no es un error
    } catch (e: HttpException) {
        Resultado.Error("El servidor respondió con un error (código ${e.code()}).")
    } catch (e: IOException) {
        Resultado.Error("No hay conexión a internet. Revisa tu red e inténtalo de nuevo.")
    } catch (e: Exception) {
        Resultado.Error("Ocurrió un error inesperado.")
    }
