package otus.homework.coroutines

sealed class Result<out T> {
    class Success<out T>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()

}