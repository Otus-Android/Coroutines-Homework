package otus.homework.coroutines.domain

sealed class Result<T>
class Success<T>(val data: T) : Result<T>()
class Error<T>(val t: Throwable) : Result<T>()
class Processing<T>(val inProcess:Boolean) : Result<T>()