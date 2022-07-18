package otus.homework.coroutines

sealed interface Result {

    data class Success<T>(val result: T) : Result
    data class Error(val message: String) : Result
}