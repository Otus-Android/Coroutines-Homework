package otus.homework.coroutines

sealed class Result {
    data class Success<out T : Any>(val catUi: T) : Result()
    data class Error(val throwable: Throwable) : Result()
}