package otus.homework.coroutines

sealed class Result
data class Error(val message: String) : Result()
data class Success<T>(val data: T): Result()

