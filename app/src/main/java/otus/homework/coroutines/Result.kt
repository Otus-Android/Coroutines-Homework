package otus.homework.coroutines

import java.lang.Exception

sealed class Result<out T : Any?> {
    data class Success<out T : Any?>(val data: T) : Result<T>()
    data class Error(val error: Exception) : Result<Nothing>()
}