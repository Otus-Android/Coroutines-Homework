package otus.homework.coroutines


sealed interface Result {
    data class Success<T>(
        val body: T,
    ): Result
    data class Error(
        val error: Throwable,
    ): Result
}