package otus.homework.coroutines

sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Error(val errorMessage: String) : Result<Nothing>()
}