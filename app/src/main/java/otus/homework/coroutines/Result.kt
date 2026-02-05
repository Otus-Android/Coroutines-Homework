package otus.homework.coroutines

internal sealed class Result<out T> {
    object None: Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorMsg: String) : Result<Nothing>()
}