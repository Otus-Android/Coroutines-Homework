package otus.homework.coroutines

sealed interface Result<out T> {

    @JvmInline
    value class Success<T>(val data: T): Result<T>

    @JvmInline
    value class Error<T>(val message: String): Result<T>
}