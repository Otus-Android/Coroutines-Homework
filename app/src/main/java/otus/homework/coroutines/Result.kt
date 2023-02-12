package otus.homework.coroutines

sealed class Result<T>(
    open val data: T,
    open val message: T? = null
) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(
        override val data: T,
        override val message: T? = null
    ) : Result<T>(data, message)
}
