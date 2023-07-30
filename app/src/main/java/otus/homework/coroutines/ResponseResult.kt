package otus.homework.coroutines

sealed interface ResponseResult<T>{
    class Success<T>(val value: T) : ResponseResult<T>
    class Error<T>(val throwable: Throwable) : ResponseResult<T>
}