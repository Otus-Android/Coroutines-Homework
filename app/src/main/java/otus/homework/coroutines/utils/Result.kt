package otus.homework.coroutines.utils


sealed class Result {
    data class Success<T>(val data: T): Result()
    data class Error(val message: String): Result()
    object Loading: Result()
}
