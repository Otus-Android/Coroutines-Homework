package otus.homework.coroutines

sealed class Result<out T, out E> {
    class Success<T>(val value: T): Result<T, Nothing>()
    class Error<E>(val error: E): Result<Nothing, E>()
}