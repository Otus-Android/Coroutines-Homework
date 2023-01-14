package otus.homework.coroutines

sealed class Result<out T, R>(val success: T? = null, val mistake: R? = null) {
    data class Success<T, R>(val info: T) : Result<T, R>(success = info)
    data class Error<T, R>(val info: R) : Result<T, R>(mistake = info)
}
