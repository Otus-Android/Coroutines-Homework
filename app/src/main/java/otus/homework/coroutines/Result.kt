package otus.homework.coroutines

sealed class Result {
    data class Success<T>(val content: T) : Result()
    data class Error(val throwable: Throwable) : Result()
}
