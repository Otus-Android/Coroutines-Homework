package otus.homework.coroutines

sealed class Result<out T> {
    data class Error(val exception: Throwable) : Result<Nothing>()
    data class Success<T>(val item: T) : Result<T>()
}
