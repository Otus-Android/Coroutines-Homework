package otus.homework.coroutines

sealed interface Result {
    class Error(val message: String) : Result
    class Success<T>(val result: T) : Result
}
