package otus.homework.coroutines.util

sealed class Result {
    object Loading : Result()
    data class Success<T>(val data: T) : Result()
    data class Error(val exception: Throwable) : Result()
}
