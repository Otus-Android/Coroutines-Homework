package otus.homework.coroutines

sealed class Result<T>(val data: T? = null) {
    class Success<T>(data: T?): Result<T>(data)
    class Error<T>(data: T?): Result<T>(data)
}
