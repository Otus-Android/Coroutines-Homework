package otus.homework.coroutines

sealed class Result {
    data class Success<T>(val model: T) : Result()
    data class Error(val e: Exception) : Result()
}