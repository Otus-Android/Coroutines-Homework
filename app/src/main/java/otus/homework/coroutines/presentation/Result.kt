package otus.homework.coroutines.presentation

sealed class Result<out T> {

    class Success<T>(val data: T): Result<T>()

    class Error(val error: Throwable): Result<Nothing>()
}
