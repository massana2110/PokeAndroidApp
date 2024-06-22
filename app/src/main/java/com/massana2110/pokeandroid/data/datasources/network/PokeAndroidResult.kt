/**
 * Author: David Massana [david.massana.10@gmail.com]
 * Description: Kotlin file to handle api result for success, error and exception cases
 */

package com.massana2110.pokeandroid.data.datasources.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * This is a wrapper class to handle all possible results in a API Call
 */
sealed class PokeApiResult<out T: Any> {
    data class Success<out T: Any>(val data: T?): PokeApiResult<T>()
    data class Error(val code: Int, val message: String): PokeApiResult<Nothing>()
    data class Exception(val exception: Throwable): PokeApiResult<Nothing>()
}

/**
 * Suspended function to make a safe api request
 * @param execute: The suspend function of retrofit api call
 * @return The PokeApiResult instance with the corresponding result
 */
suspend fun <T: Any> apiRequest(
    execute: suspend () -> Response<T>
): PokeApiResult<T> {
    return try {
        val response = withContext(Dispatchers.IO) { execute() }
        val body = response.body()

        if (response.isSuccessful) PokeApiResult.Success(body)
        else PokeApiResult.Error(code = response.code(), response.message())

    } catch (e: HttpException) {
        PokeApiResult.Exception(e)
    } catch (e: Throwable) {
        PokeApiResult.Exception(e)
    }
}