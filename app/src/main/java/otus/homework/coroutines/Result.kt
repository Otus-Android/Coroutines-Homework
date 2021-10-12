package otus.homework.coroutines

sealed class Result

data class Success<T>(val result: T) : Result()
data class Error(val error: String) : Result()
