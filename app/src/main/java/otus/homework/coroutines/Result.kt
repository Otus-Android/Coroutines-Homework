package otus.homework.coroutines

sealed class Result {

    data class Success<T>(val value: T) : Result()
    data class Error(val message: String) : Result()
}
