package otus.homework.coroutines


sealed class Result {
    data class Success<out T>(val catData: T) : Result()
    data class Error(val throwable: Throwable) : Result()
}