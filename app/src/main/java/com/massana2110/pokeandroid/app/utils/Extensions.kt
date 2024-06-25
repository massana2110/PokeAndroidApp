package com.massana2110.pokeandroid.app.utils

inline fun <T, R> Result<T>.flatMap(transform: (value: T) -> Result<R>): Result<R> =
    fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )

inline fun <T, R> Result<T>.map(transform: (value: T) -> R): Result<R> =
    fold(
        onSuccess = { Result.success(transform(it)) },
        onFailure = { Result.failure(it) }
    )