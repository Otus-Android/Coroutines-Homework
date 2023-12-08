package otus.homework.coroutines


sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val errorMessage: String?) : Result()
}
