package otus.homework.coroutines.presentation

sealed class Result<T>(val data: T? = null, val throwable: Throwable? = null) {
    class Success<T>(data: T): Result<T>(data = data)
    class Error<T>(throwable: Throwable) : Result<T>(throwable = throwable)
}