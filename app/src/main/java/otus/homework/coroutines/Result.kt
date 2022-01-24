package otus.homework.coroutines

sealed class Result<out T> {
    data class Success<out T>(val value: T): Result<T>()
    data class GenericError(val error: Exception? = null): Result<Nothing>()
}