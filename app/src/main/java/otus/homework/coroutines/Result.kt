package otus.homework.coroutines

sealed class Result<out T> {
    data class Success<T>(val entity: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}