package otus.homework.coroutines

sealed class Result<out T> {
    data class Success<T>(val response: T): Result<T>()
    data class Error(val e: Exception): Result<Nothing>()
}