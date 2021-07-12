package otus.homework.coroutines

sealed class Result<T>(
    val data: T? = null,
    val err: Throwable? = null
) {
    class Loading<T> : Result<T>()
    class Success<T>(res: T) : Result<T>(data = res)
    class Error<T>(msg: Throwable) : Result<T>(err = msg)
}


