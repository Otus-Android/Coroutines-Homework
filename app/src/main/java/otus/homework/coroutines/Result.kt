package otus.homework.coroutines

sealed class Result<T : Any> {
    data class Loading<T : Any>(val message: String) : Result<T>()
    data class Success<T : Any>(val data: T) : Result<T>()
    data class Error<T : Any>(val message: String) : Result<T>()
}
