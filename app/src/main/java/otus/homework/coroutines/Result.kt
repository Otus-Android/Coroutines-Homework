package otus.homework.coroutines

sealed class Result<out T: Any?> {
    data class Success<out T: Any?>(val data: T): Result<T>()
    data class Error(val msg: String?, val cause: Exception? = null): Result<Nothing>()
}
