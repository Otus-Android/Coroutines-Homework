package otus.homework.coroutines

sealed class Result<T> {
    class Loading<T> : Result<T>()
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val err: Throwable) : Result<T>()
}


