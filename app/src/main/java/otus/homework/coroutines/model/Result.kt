package otus.homework.coroutines.model

sealed class Result {
    data class Success<T>(val data: T): Result()
    data class Error(val exception: Throwable): Result()
}