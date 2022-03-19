package otus.homework.coroutines.data

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error(val message: String?) : Result<Nothing>()
}
