package otus.homework.coroutines.models

sealed class Result<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T> (data: T): Result<T>(data = data)
    class Error<T> (error: String): Result<T>(error = error)
}
